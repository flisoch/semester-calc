package ru.itis.cal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.cal.dto.CalendarEventDto;
import ru.itis.cal.service.CalendarEventService;

@RestController
@RequestMapping("/api/classes/events")
public class CalendarEventController {
    private final CalendarEventService calendarEventService;
    @Autowired
    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CalendarEventDto> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(calendarEventService.getEvent(id));
    }

    @PostMapping
    public ResponseEntity saveClassEvent(@RequestBody CalendarEventDto classEvent) {
        return ResponseEntity.ok(calendarEventService.saveClassEvent(classEvent));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteClassEvent(@PathVariable Long id) {
        calendarEventService.deleteClassEvent(id);
        return (ResponseEntity) ResponseEntity.ok();
    }
}
