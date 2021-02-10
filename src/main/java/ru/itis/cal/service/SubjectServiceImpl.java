package ru.itis.cal.service;

import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.cal.domain.Class;
import ru.itis.cal.domain.CreditType;
import ru.itis.cal.domain.StudentUser;
import ru.itis.cal.domain.Subject;
import ru.itis.cal.dto.SemesterStats;
import ru.itis.cal.dto.SubjectDto;
import ru.itis.cal.dto.queryFilter.SubjectQueryFilter;
import ru.itis.cal.repository.ScheduleClassRepository;
import ru.itis.cal.repository.SubjectRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final StudentUserService studentUserService;
    private final ScheduleClassRepository classRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, StudentUserService studentUserService, ScheduleClassRepository classRepository) {
        this.subjectRepository = subjectRepository;
        this.studentUserService = studentUserService;
        this.classRepository = classRepository;
    }

    @Override
    @Transactional
    public List<SubjectDto> getSubjectsBy(SubjectQueryFilter filter) {

        List<Subject> subjectsFiltered = subjectRepository.findAllByFilter(filter);
        return subjectsFiltered.stream().map(SubjectDto::from).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public SubjectDto getSubject(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        return subject.map(SubjectDto::from).orElse(null);
    }

    @Override
    @Transactional
    public SemesterStats getSemesterStatsByGroupOrUser(String groupNumber, HttpServletRequest request) {
        List<Class> classes;
        StudentUser user = studentUserService.getUser(request);
        if (user != null) {
            classes = user.getClasses();
        } else {
            classes = classRepository.findByGroups_number(groupNumber);
        }
        List<Subject> subjects = classes.stream()
                .map(Class::getSubject)
                .distinct()
                .collect(Collectors.toList());
        List<Subject> electives = StreamEx.of(subjects)
                .filter(Subject::isElective)
                .distinct(Subject::getDescriptor)
                .toList();
        List<Subject> notElectives = subjects.stream()
                .filter(subject -> !subject.isElective())
                .collect(Collectors.toList());
        int subjectsCount = electives.size() + notElectives.size();
        int examsCount = Math.toIntExact(notElectives.stream()
                .filter(subject -> subject.getCreditType().equals(CreditType.EXAM))
                .count())
                +
                Math.toIntExact(electives.stream()
                        .filter(subject -> subject.getCreditType().equals(CreditType.EXAM))
                        .count());
        int creditsCount = Math.toIntExact(notElectives.stream()
                .filter(subject -> subject.getCreditType().equals(CreditType.CREDIT))
                .count())
                +
                Math.toIntExact(electives.stream()
                        .filter(subject -> subject.getCreditType().equals(CreditType.EXAM))
                        .count())
                ;
        int classesPerWeek = Math.toIntExact(notElectives.stream()
                .map(subject -> subject.getClasses().size())
                .reduce(0, Integer::sum)
        )
                +
                Math.toIntExact(electives.stream()
                        .map(subject -> subject.getClasses().size())
                        .reduce(0, Integer::sum));
        double classesPerDay = classesPerWeek / 6.0;
        LocalDate todayDate = LocalDate.now();
        DayOfWeek dow = todayDate.getDayOfWeek();
        int classesToday = Math.toIntExact(classes.stream()
                .filter(c -> c.getCalendarEvent().getWeekDay().equals(dow))
                .count());
        double hoursPerWeek = classesPerWeek * 1.5;
        LocalDate lastDayOfYear = todayDate.with(lastDayOfYear());
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = todayDate.get(woy);
        int weeksUntilExams = lastDayOfYear.get(woy) - weekNumber;
        return SemesterStats.builder()
                .subjectsCount(subjectsCount)
                .examsCount(examsCount)
                .creditsCount(creditsCount)
                .classesPerWeek(classesPerWeek)
                .classesPerDay(classesPerDay)
                .classesToday(classesToday)
                .hoursPerWeek(hoursPerWeek)
                .weeksUntilExams(weeksUntilExams)
                .build();
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
