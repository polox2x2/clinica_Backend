package com.Clinica.Practica01.feature.medico.service;

import com.Clinica.Practica01.feature.medico.dto.SpecialityResponseDto;
import com.Clinica.Practica01.feature.medico.dto.SpecialityRequestDto;
import java.util.List;

public interface SpecialityService {
    List<SpecialityResponseDto> findAll();
    SpecialityResponseDto findById(Long id);
    SpecialityResponseDto create(SpecialityRequestDto request);
    SpecialityResponseDto update(Long id, SpecialityRequestDto request);
    void delete(Long id);
}
