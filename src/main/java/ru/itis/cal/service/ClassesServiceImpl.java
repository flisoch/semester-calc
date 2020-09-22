package ru.itis.cal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.cal.domain.Class;
import ru.itis.cal.domain.StudentUser;
import ru.itis.cal.dto.ClassDto;
import ru.itis.cal.repository.ScheduleClassRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassesServiceImpl implements ClassesService {

    private final ScheduleClassRepository classRepository;
    private final StudentUserService studentUserService;

    @Autowired
    public ClassesServiceImpl(ScheduleClassRepository classRepository, StudentUserService studentUserService) {
        this.classRepository = classRepository;
        this.studentUserService = studentUserService;
    }

    @Override
    public ClassDto getScheduleClass(Long id) {
        Optional<Class> scheduleClass = classRepository.findById(id);
        return scheduleClass.map(ClassDto::from).orElse(null);
    }

    @Override
    public List<ClassDto> getClassesBy(String groupNumber) {
        List<Class> classes = classRepository.findByGroups_number(groupNumber);
        return classes.stream().map(ClassDto::from).collect(Collectors.toList());
    }


    @Override
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<ClassDto> getClassesByUser(HttpServletRequest request) {
        StudentUser user = studentUserService.getUser(request);
        return user.getClasses().stream().map(ClassDto::from).collect(Collectors.toList());
    }
}
