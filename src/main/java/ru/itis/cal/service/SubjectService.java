package ru.itis.cal.service;

import ru.itis.cal.controller.queryFilter.SubjectQueryFilter;
import ru.itis.cal.dto.SubjectDto;

import java.util.List;


public interface SubjectService {
    List<SubjectDto> getSubjectsBy(SubjectQueryFilter filter);
    SubjectDto getSubject(Long id);
}
