package ru.itis.cal.dto;

import com.sun.xml.internal.ws.api.server.SDDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.*;

import java.util.List;
import java.util.stream.Collectors;

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
    private HoursDto hours;
    private List<ClassDto> classes;
    private boolean isElective;
    private ElectivesDescriptorDto descriptor;

    public static SubjectDto from(Subject subject) {
        SubjectDto subjectDto = SubjectDto.builder()
                .id(subject.getId())
                .name(subject.getName())
                .course(CourseDto.from(subject.getCourse()))
                .isElective(subject.isElective())
                .creditType(subject.getCreditType())
                .syllabus(subject.getSyllabus())
                .hours(HoursDto.from(subject.getHours()))
                .classes(subject.getClasses().stream().map(ClassDto::from).collect(Collectors.toList()))
                .build();
        if (subject.isElective()) {
            subjectDto.setDescriptor(ElectivesDescriptorDto.from(subject.getDescriptor()));
        }

        return subjectDto;
    }
}
