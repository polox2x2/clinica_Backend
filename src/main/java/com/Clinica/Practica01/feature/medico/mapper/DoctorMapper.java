package com.Clinica.Practica01.feature.medico.mapper;

import com.Clinica.Practica01.feature.medico.dto.DoctorRequestDto;
import com.Clinica.Practica01.feature.medico.dto.DoctorResponseDto;
import com.Clinica.Practica01.feature.medico.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequestDto dto) {
        if (dto == null) return null;
        return Doctor.builder()
            // .field(dto.getField())
            .build();
    }

    public DoctorResponseDto toDto(Doctor entity) {
        if (entity == null) return null;
        DoctorResponseDto dto = new DoctorResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
