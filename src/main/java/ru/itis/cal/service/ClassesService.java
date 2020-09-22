package ru.itis.cal.service;

import ru.itis.cal.domain.Class;
import ru.itis.cal.dto.ClassDto;

import java.util.List;

public interface ClassesService {
    ClassDto getScheduleClass(Long id);

    List<ClassDto> getClassesBy(String groupNumber);

    void deleteClass(Long id);

    List<Class> saveAll(List<Class> classes);
}

