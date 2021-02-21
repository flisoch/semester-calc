package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.Hours;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoursDto {
    private Long id;
    private Integer lecture;
    private Integer seminar;
    private Integer practice;
    private Integer laboratory;
    private Integer selfStudy;
    private Integer school;

    public static HoursDto from(Hours hours) {
        if (hours == null) {
            return HoursDto.builder().build();
        }
        Integer totalInHouse = hours.getLecture() + hours.getSeminar() + hours.getPractice() + hours.getLaboratory();
        return HoursDto.builder()
                .id(hours.getId())
                .lecture(hours.getLecture())
                .seminar(hours.getSeminar())
                .practice(hours.getPractice())
                .laboratory(hours.getLaboratory())
                .selfStudy(hours.getSelfDependent())
                .school(totalInHouse)
                .build();
    }
}
