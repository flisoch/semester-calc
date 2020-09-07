package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.*;
import ru.itis.cal.domain.Class;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto {

    private Long id;
    private ClassType classType;
    private String classroomNumber;

    public static ClassDto from(Class aClass) {
        return ClassDto.builder()
                .id(aClass.getId())
                .classType(aClass.getClassType())
                .classroomNumber(aClass.getClassroomNumber())
                .build();
    }
}
