package ru.itis.cal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.util.DayOfWeekIntegerConverter;
import ru.itis.cal.util.LocalDateTimeConverter;
import ru.itis.cal.util.LocalTimeConverter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
    @Convert(converter = DayOfWeekIntegerConverter.class)
    private DayOfWeek weekDay;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime startTime;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime endTime;
    private Integer repeatsNumber;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime repeatsUntilDate;
    @OneToOne
    private Class scheduleClass;
}
