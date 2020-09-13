package ru.itis.cal.service;

import com.google.api.services.calendar.model.Event;
import ru.itis.cal.dto.ClassDto;

import java.util.List;

public interface GoogleService {
    List<Event> mapClassesToEvents(List<ClassDto> classes);
}
