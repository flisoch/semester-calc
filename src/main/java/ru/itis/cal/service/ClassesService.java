package ru.itis.cal.service;

import ru.itis.cal.dto.ClassDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClassesService {
    ClassDto getScheduleClass(Long id);

    List<ClassDto> getClassesBy(String groupNumber);

    void deleteClass(Long id);

    List<ClassDto> getClassesByUser(HttpServletRequest request);
}

