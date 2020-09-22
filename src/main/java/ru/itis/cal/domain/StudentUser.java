package ru.itis.cal.domain;

import com.google.api.client.auth.oauth2.Credential;
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
public class StudentUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cookieId;
    @ManyToOne
    @JoinColumn(name = "groud_id")
    private Group group;
    @ElementCollection(targetClass = String.class)
    private List<String> googleCalendarIds;
    @Transient
    Credential credential;
    @ManyToMany
    @JoinTable(name = "Class_student",
            joinColumns = {@JoinColumn(name = "student_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "class_id")}
    )
    private List<Class> classes;
}
