package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.Teacher;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    private Long id;
    private String name;
    private String middleName;
    private String lastName;
    private String kpfuLink;

    public static TeacherDto from(Teacher teacher) {
        if (teacher == null) {
            return TeacherDto.builder().build();
        }
        return TeacherDto.builder()
                .id(teacher.getId())
                .lastName(teacher.getLastName())
                .name(teacher.getName())
                .middleName(teacher.getMiddleName())
                .kpfuLink(teacher.getKpfuLink())
                .build();
    }
}
