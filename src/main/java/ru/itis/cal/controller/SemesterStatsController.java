package ru.itis.cal.controller;

import com.google.api.client.auth.oauth2.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.cal.dto.SemesterStats;
import ru.itis.cal.service.SubjectService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/semester")
public class SemesterStatsController {
    @Autowired
    private final SubjectService subjectService;

    public SemesterStatsController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<SemesterStats> getSubjectNames(@RequestParam(required = false, defaultValue = "11-701") String groupNumber,
                                                         HttpServletRequest request) {
        SemesterStats semesterStats = subjectService.getSemesterStatsByGroupOrUser(groupNumber, request);
        return ResponseEntity.ok(semesterStats);
    }
}
