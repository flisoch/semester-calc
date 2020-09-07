package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.Course;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Long id;
    private Integer number;

    public static CourseDto from(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .number(course.getNumber())
                .build();
    }
}
