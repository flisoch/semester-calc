package ru.itis.cal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.cal.dto.ClassDto;
import ru.itis.cal.dto.queryFilter.ClassesQueryFilter;
import ru.itis.cal.service.ClassesService;

import java.util.List;

@RestController
@RequestMapping("/api/schedule-classes")
public class ScheduleClassesController {
    private final ClassesService classesService;

    public ScheduleClassesController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping
    public ResponseEntity<List<ClassDto>> getClassesBy(ClassesQueryFilter filter) {
        return ResponseEntity.ok(classesService.getClassesBy(filter.getGroupNumber()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ClassDto> getClassById(@PathVariable Long id) {
        return ResponseEntity.ok(classesService.getScheduleClass(id));
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteClass(@PathVariable Long id) {
        classesService.deleteClass(id);
        return (ResponseEntity) ResponseEntity.ok();
    }
}
