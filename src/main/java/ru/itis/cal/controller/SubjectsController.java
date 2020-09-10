package ru.itis.cal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.cal.controller.queryFilter.SubjectQueryFilter;
import ru.itis.cal.dto.SubjectDto;
import ru.itis.cal.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectsController {
    private final SubjectService subjectService;

    public SubjectsController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getSubjectNames(SubjectQueryFilter filter) {
        return ResponseEntity.ok(subjectService.getSubjectsBy(filter));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SubjectDto> getSubjectNames(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubject(id));
    }

}
