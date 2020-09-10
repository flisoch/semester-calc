package ru.itis.cal.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Date.valueOf(localDateTime.toLocalDate());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date timestamp) {
        return timestamp == null ? null :LocalDateTime.of(timestamp.toLocalDate(), LocalTime.MIN);
    }

}