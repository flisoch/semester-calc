package ru.itis.cal.repository;

import ru.itis.cal.controller.queryFilter.SubjectQueryFilter;
import ru.itis.cal.domain.Subject;

import java.util.List;

public interface SubjectRepositoryCustom {
    List<Subject> findAllByFilter(SubjectQueryFilter filter);
}
