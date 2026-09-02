package com.Clinica.Practica01.feature.paciente.mapper;

import com.Clinica.Practica01.feature.paciente.dto.PatientRequestDto;
import com.Clinica.Practica01.feature.paciente.dto.PatientResponseDto;
import com.Clinica.Practica01.feature.paciente.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequestDto dto) {
        if (dto == null) return null;
        return Patient.builder()
            // .field(dto.getField())
            .build();
    }

    public PatientResponseDto toDto(Patient entity) {
        if (entity == null) return null;
        PatientResponseDto dto = new PatientResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
