package com.Clinica.Practica01.feature.medico.mapper;

import com.Clinica.Practica01.feature.medico.dto.SpecialityRequestDto;
import com.Clinica.Practica01.feature.medico.dto.SpecialityResponseDto;
import com.Clinica.Practica01.feature.medico.entity.Speciality;
import org.springframework.stereotype.Component;

@Component
public class SpecialityMapper {

    public Speciality toEntity(SpecialityRequestDto dto) {
        if (dto == null) return null;
        return Speciality.builder()
            // .field(dto.getField())
            .build();
    }

    public SpecialityResponseDto toDto(Speciality entity) {
        if (entity == null) return null;
        SpecialityResponseDto dto = new SpecialityResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
