package ru.itis.cal.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "subject")
public class Hours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer lecture;
    private Integer seminar;
    private Integer practice;
    private Integer laboratory;
    private Integer selfDependent;

    @OneToOne
    private Subject subject;

    public Integer totalInHouse() {
        return lecture+seminar+practice+laboratory;
    }

    public Double ratioSelfStudyToInHouse() {
        return new Double(this.selfDependent) /totalInHouse();
    }
}
