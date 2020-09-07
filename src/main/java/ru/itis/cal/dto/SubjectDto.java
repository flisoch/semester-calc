package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.CreditType;
import ru.itis.cal.domain.Hours;
import ru.itis.cal.domain.Subject;
import ru.itis.cal.domain.Syllabus;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private Long id;
    private String name;
    private CourseDto course;
    private Syllabus syllabus;
    private CreditType creditType;
    private Hours hours;
    private List<ClassDto> classes;
    private boolean isElective;
    private SubjectDto parentSubject;
    private List<SubjectDto> electives;

    public static SubjectDto from(Subject subject) {
        return SubjectDto.builder()
                .id(subject.getId())
                .name(subject.getName())
                .course(CourseDto.from(subject.getCourse()))
                .syllabus(subject.getSyllabus())
                .creditType(subject.getCreditType())
//                .hours(subject.getHours())
                .isElective(subject.isElective())
                .build();
    }
}
