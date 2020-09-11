package ru.itis.cal.service;

import ru.itis.cal.dto.queryFilter.SubjectQueryFilter;
import ru.itis.cal.dto.CalendarEventDto;

import java.util.List;

public interface CalendarEventService {
    List<CalendarEventDto> getEvents(SubjectQueryFilter filter);

    CalendarEventDto getEvent(Long id);

    CalendarEventDto saveClassEvent(CalendarEventDto classEvent);

    void deleteClassEvent(Long id);
}
