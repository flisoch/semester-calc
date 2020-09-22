package ru.itis.cal.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;

@Converter(autoApply = true)
public class DayOfWeekIntegerConverter implements AttributeConverter<DayOfWeek, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        return dayOfWeek==null?null:dayOfWeek.getValue();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer integer) {
        return integer==null?null:DayOfWeek.of(integer);
    }
}
