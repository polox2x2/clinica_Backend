package com.Clinica.Practica01.feature.cita.mapper;

import com.Clinica.Practica01.feature.cita.dto.AppointmentRequestDto;
import com.Clinica.Practica01.feature.cita.dto.AppointmentResponseDto;
import com.Clinica.Practica01.feature.cita.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentRequestDto dto) {
        if (dto == null) return null;
        return Appointment.builder()
            // .field(dto.getField())
            .build();
    }

    public AppointmentResponseDto toDto(Appointment entity) {
        if (entity == null) return null;
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
