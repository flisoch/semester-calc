package ru.itis.cal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.cal.dto.ClassDto;
import ru.itis.cal.dto.ClassIdDto;
import ru.itis.cal.service.ClassesService;
import ru.itis.cal.service.StudentUserService;

@RestController
@RequestMapping("/api/students")
public class StudentUserController {
    private final ClassesService classesService;
    private final StudentUserService studentUserService;

    @Autowired
    public StudentUserController(ClassesService classesService, StudentUserService studentUserService) {
        this.classesService = classesService;
        this.studentUserService = studentUserService;
    }

    @PostMapping(path = "/{id}/classes")
    public ResponseEntity<ClassDto> AddClassToUserSchedule(@PathVariable Long id, @RequestBody ClassIdDto classIdDto) {
        return ResponseEntity.ok(studentUserService.saveClassesById(id, classIdDto.getId()));
    }


    @DeleteMapping(path = "/{userId}/classes/{classId}")
    public ResponseEntity removeClassFromUserSchedule(@PathVariable Long userId, @PathVariable Long classId) {
        studentUserService.removeClassFromUserSchedule(userId, classId);
        return ResponseEntity.status(HttpStatus.OK).body("class has been removed successfully");
    }
}
