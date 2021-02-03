package ru.itis.cal.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "hours")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "course")
    private Course course;
    @OneToOne
    private Syllabus syllabus;
    @Enumerated(EnumType.STRING)
    private CreditType creditType;
    @OneToOne
    private Hours hours;
    @OneToMany(mappedBy = "subject")
    private List<Class> classes;
    private boolean elective;
    @ManyToOne
    @JoinColumn(name = "electives_desctiptor")
    private ElectivesDescriptor descriptor;
    @ManyToMany(mappedBy = "classes")
    private List<StudentUser> studentUsers;

}
