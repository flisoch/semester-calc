package ru.itis.cal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.boot.context.properties.bind.DefaultValue;
import ru.itis.cal.domain.CalendarEvent;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDto {
    private Long id;
    private DayOfWeek weekDay;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer repeatsNumber;
    private String repeatsUntilDate;

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
            eventDto.setRepeatsUntilDate(event.getRepeatsUntilDate().toString());
        }
        return eventDto;
    }

}
