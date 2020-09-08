package ru.itis.cal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.cal.controller.queryFilter.SubjectQueryFilter;
import ru.itis.cal.domain.Subject;
import ru.itis.cal.dto.SubjectDto;
import ru.itis.cal.repository.SubjectRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
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


}
