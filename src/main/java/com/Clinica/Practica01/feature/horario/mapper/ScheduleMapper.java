package com.Clinica.Practica01.feature.horario.mapper;

import com.Clinica.Practica01.feature.horario.dto.ScheduleRequestDto;
import com.Clinica.Practica01.feature.horario.dto.ScheduleResponseDto;
import com.Clinica.Practica01.feature.horario.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public Schedule toEntity(ScheduleRequestDto dto) {
        if (dto == null) return null;
        return Schedule.builder()
            // .field(dto.getField())
            .build();
    }

    public ScheduleResponseDto toDto(Schedule entity) {
        if (entity == null) return null;
        ScheduleResponseDto dto = new ScheduleResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
