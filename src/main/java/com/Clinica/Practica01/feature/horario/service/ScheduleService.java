package com.Clinica.Practica01.feature.horario.service;

import com.Clinica.Practica01.feature.horario.dto.ScheduleResponseDto;
import com.Clinica.Practica01.feature.horario.dto.ScheduleRequestDto;
import java.util.List;

public interface ScheduleService {
    List<ScheduleResponseDto> findAll();
    ScheduleResponseDto findById(Long id);
    ScheduleResponseDto create(ScheduleRequestDto request);
    ScheduleResponseDto update(Long id, ScheduleRequestDto request);
    void delete(Long id);
}
