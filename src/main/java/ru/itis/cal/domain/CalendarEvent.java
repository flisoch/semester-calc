package ru.itis.cal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.dto.CalendarEventDto;
import ru.itis.cal.util.LocalDateConverter;
import ru.itis.cal.util.LocalTimeConverter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DayOfWeek weekDay;
    @Convert(converter = LocalTimeConverter.class)
    private LocalTime startTime;
    @Convert(converter = LocalTimeConverter.class)
    private LocalTime endTime;
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;
    private Integer repeatsNumber;
    @Convert(converter = LocalDateConverter.class)
    private LocalDate repeatsUntilDate;

}
