package com.Clinica.Practica01.feature.medico.service;

import com.Clinica.Practica01.feature.medico.dto.DoctorResponseDto;
import com.Clinica.Practica01.feature.medico.dto.DoctorRequestDto;
import java.util.List;

public interface DoctorService {
    List<DoctorResponseDto> findAll();
    DoctorResponseDto findById(Long id);
    DoctorResponseDto create(DoctorRequestDto request);
    DoctorResponseDto update(Long id, DoctorRequestDto request);
    void delete(Long id);
}
