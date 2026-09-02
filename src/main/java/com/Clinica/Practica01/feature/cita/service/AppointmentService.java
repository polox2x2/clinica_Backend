package com.Clinica.Practica01.feature.cita.service;

import com.Clinica.Practica01.feature.cita.dto.AppointmentResponseDto;
import com.Clinica.Practica01.feature.cita.dto.AppointmentRequestDto;
import java.util.List;

public interface AppointmentService {
    List<AppointmentResponseDto> findAll();
    AppointmentResponseDto findById(Long id);
    AppointmentResponseDto create(AppointmentRequestDto request);
    AppointmentResponseDto update(Long id, AppointmentRequestDto request);
    void delete(Long id);
}
