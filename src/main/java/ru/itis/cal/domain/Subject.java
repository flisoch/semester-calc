package ru.itis.cal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private CreditType creditType;
    @OneToOne
    private Hours hours;
    @OneToMany(mappedBy = "subject")
    private List<Class> classes;
    private boolean isElective;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_subject_id")
    private Subject parentSubject;
    @OneToMany(mappedBy="parentSubject")
    private List<Subject> electives;

}