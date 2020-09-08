package ru.itis.cal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.cal.domain.ElectivesDescriptor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectivesDescriptorDto {
    private Long id;
    private String code;
    private String name;

    public static ElectivesDescriptorDto from(ElectivesDescriptor descriptor) {
        if (descriptor == null) {
            return ElectivesDescriptorDto.builder().build();
        }
        return ElectivesDescriptorDto.builder()
                .id(descriptor.getId())
                .code(descriptor.getCode())
                .name(descriptor.getName())
                .build();
    }
}
