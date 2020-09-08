package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.ElectivesDescriptor;
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
    private Integer selfDependent;

    public static HoursDto from (Hours hours) {
        if (hours==null) {
            return HoursDto.builder().build();
        }
        return HoursDto.builder()
                .id(hours.getId())
                .lecture(hours.getLecture())
                .seminar(hours.getSeminar())
                .practice(hours.getPractice())
                .laboratory(hours.getLaboratory())
                .selfDependent(hours.getSelfDependent())
                .build();
    }
}
