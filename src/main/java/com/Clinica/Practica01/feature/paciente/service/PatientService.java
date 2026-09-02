package com.Clinica.Practica01.feature.paciente.service;

import com.Clinica.Practica01.feature.paciente.dto.PatientResponseDto;
import com.Clinica.Practica01.feature.paciente.dto.PatientRequestDto;
import java.util.List;

public interface PatientService {
    List<PatientResponseDto> findAll();
    PatientResponseDto findById(Long id);
    PatientResponseDto create(PatientRequestDto request);
    PatientResponseDto update(Long id, PatientRequestDto request);
    void delete(Long id);
}
