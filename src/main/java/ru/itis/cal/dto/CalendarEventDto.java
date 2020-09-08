package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.CalendarEvent;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDto {
    private Long id;
    private DayOfWeek weekDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private Integer repeatsNumber;
    private LocalDate repeatsUntilDate;

    public static CalendarEventDto from (CalendarEvent event) {
        if (event == null) {
            return CalendarEventDto.builder().build();
        }
        CalendarEventDto eventDto = CalendarEventDto.builder()
                .id(event.getId())
                .weekDay(event.getWeekDay())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .build();
        if (event.getRepeatsNumber() != null) {
            eventDto.setRepeatsNumber(event.getRepeatsNumber());
        }
        else if (event.getRepeatsUntilDate() != null) {
            eventDto.setRepeatsUntilDate(event.getRepeatsUntilDate());
        }
        return eventDto;
    }

}
