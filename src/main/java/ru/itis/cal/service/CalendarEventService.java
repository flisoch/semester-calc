package ru.itis.cal.service;

import ru.itis.cal.dto.CalendarEventDto;

public interface CalendarEventService {

    CalendarEventDto getEvent(Long id);

    CalendarEventDto saveClassEvent(CalendarEventDto classEvent);

    void deleteClassEvent(Long id);
}
