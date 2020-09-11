package ru.itis.cal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.cal.dto.queryFilter.SubjectQueryFilter;
import ru.itis.cal.domain.CalendarEvent;
import ru.itis.cal.dto.CalendarEventDto;
import ru.itis.cal.repository.CalendarEventRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarEventServiceImpl implements CalendarEventService {
    private final CalendarEventRepository calendarEventRepository;

    @Autowired
    public CalendarEventServiceImpl(CalendarEventRepository calendarEventRepository) {
        this.calendarEventRepository = calendarEventRepository;
    }

    @Override
    public List<CalendarEventDto> getEvents(SubjectQueryFilter filter) {
        return null;
    }

    @Override
    public CalendarEventDto getEvent(Long id) {
        Optional<CalendarEvent> event = calendarEventRepository.findById(id);
        return event.map(CalendarEventDto::from).orElse(null);
    }

    @Override
    @Transactional
    public CalendarEventDto saveClassEvent(CalendarEventDto classEventDto) {
        CalendarEvent calendarEvent = buildFromForm(classEventDto);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return CalendarEventDto.from(calendarEvent);
    }

    @Override
    public void deleteClassEvent(Long id) {
        calendarEventRepository.deleteById(id);
    }

    private CalendarEvent buildFromForm(CalendarEventDto classEventDto) {
        CalendarEvent event = CalendarEvent.builder()
                .weekDay(classEventDto.getWeekDay())
                .startTime(classEventDto.getStartTime())
                .endTime(classEventDto.getEndTime())
                .startDate(LocalDateTime.of(LocalDate.parse(classEventDto.getStartDate()), LocalTime.MIN))
                .build();
        if (classEventDto.getRepeatsNumber() != null) {
            event.setRepeatsNumber(classEventDto.getRepeatsNumber());
        }
        else if (classEventDto.getRepeatsUntilDate() != null) {
            event.setRepeatsUntilDate(LocalDateTime.of(LocalDate.parse(classEventDto.getRepeatsUntilDate()), LocalTime.MIN));
        }
        return event;
    }

}
