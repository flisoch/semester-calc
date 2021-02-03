package ru.itis.cal.service;

import ru.itis.cal.dto.SemesterStats;
import ru.itis.cal.dto.queryFilter.SubjectQueryFilter;
import ru.itis.cal.dto.SubjectDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface SubjectService {
    List<SubjectDto> getSubjectsBy(SubjectQueryFilter filter);
    SubjectDto getSubject(Long id);
    SemesterStats getSemesterStatsByGroupOrUser(String groupNumber, HttpServletRequest request);
}
