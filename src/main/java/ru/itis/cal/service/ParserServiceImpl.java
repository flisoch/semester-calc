package ru.itis.cal.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.cal.domain.*;
import ru.itis.cal.domain.Class;
import ru.itis.cal.google.ScheduleParser;
import ru.itis.cal.repository.SubjectRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParserServiceImpl implements ParserService {

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public List<List<Object>> getRawClasses(Sheets service, String spreadsheetId, String sheetTitle, String group) throws IOException {
        String column = ScheduleParser.getGroupRange().get(group);
        String range = sheetTitle + "!" + column + "3:" + column + "44";
        ValueRange formatted_value = service.spreadsheets().values().get(spreadsheetId, range).setValueRenderOption("FORMATTED_VALUE").execute();
        List<List<Object>> values = formatted_value.getValues();
        return values;
    }

    @Override
    public List<Class> mapToClasses(List<List<Object>> rawClasses) {
        int maxClassesPerDay = 7;
        int weekDays = 6;
        for (int i = 0; i < weekDays; i++) {
            for (int j = 0; j < maxClassesPerDay; j++) {
                int index = i*maxClassesPerDay + j;
                if(index >= rawClasses.size()) {
                    break;
                }
                List<Object> cell = rawClasses.get(index);
                if (!cell.isEmpty()) {
                    List<Class> c = parseCellData((String)cell.get(0), i, j);

                }
            }
        }
        return null;
    }

    private List<Class> parseCellData(String cell, int weekDay, int classOrder) {
        List<Class> classes = new ArrayList<>();
//        Pattern pattern = Pattern.compile("(^.+)!(.+):(.+$)");
        Pattern classesPatter = Pattern.compile("(?<elective>[А-Я][а-я\\s]*:) + " +
                "(?<classroom>\\d{3,4})" +
                "(?<teacher>[A-Я][а-я]*\\s([А-Я]\\.){1,2})" +
                "(?<subject>:+\\s*[А-Я][а-я\\s]*|^\\s*[А-Я][а-я\\s]*)"
        );
//        Pattern classroomPattern = Pattern.compile("\\d{3,4}");
//        Pattern teacherPattern = Pattern.compile("[A-Я][а-я]*\\s([А-Я]\\.){1,2}");
//        Pattern subjectPattern = Pattern.compile(":+\\s*[А-Я][а-я\\s]*|^\\s*[А-Я][а-я\\s]*");
        Matcher matcher = classesPatter.matcher(cell);
        System.out.println(matcher.matches());
        while (matcher.find()) {
            String elective = matcher.group("elective");
            String classroomNumber = matcher.group("classroom");
            String teacherName = matcher.group("teacher");
            String subjectTitle = matcher.group("subject");
            System.out.println(elective + " " + classroomNumber + " " + teacherName + " " + subjectTitle);
        }


//        ClassType classType = classTypeFromClassroom(classroomNumber);
//        CalendarEvent calendarEvent = CalendarEvent.builder().build();
//        Subject subject = getSubjectByTitle(subjectTitle, isElective);
//        Teacher teacher = getTeacherByName(teacherName);
//        Class c = Class.builder()
//                .calendarEvent(calendarEvent)
//                .classroomNumber(classroomNumber)
//                .classType(classType)
//                .subject(subject)
//                .teacher(teacher)
//                .build();
//        classes.add(c);
        return classes;
    }

    private Teacher getTeacherByName(String teacherName) {
        return null;
    }

    private ClassType classTypeFromClassroom(String classroomNumber) {
        return classroomNumber.length() == 3?ClassType.LECTURE:ClassType.SEMINAR;
    }

    private Subject getSubjectByTitle(String subjectTitle, boolean isElective) {
//        return subjectRepository.findBySubject_name();
        return null;
    }
}
