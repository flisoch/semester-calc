package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterStats {
    private Integer subjectsCount;
    private Integer examsCount;
    private Integer creditsCount;
    private Integer classesPerWeek;
    private Double classesPerDay;
    private Integer classesToday;
    private Double hoursPerWeek;
    private Integer weeksUntilExams;
}
