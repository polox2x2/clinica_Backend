package com.Clinica.Practica01.feature.medico.service;

import com.Clinica.Practica01.feature.medico.dto.SpecialityResponseDto;
import com.Clinica.Practica01.feature.medico.dto.SpecialityRequestDto;
import com.Clinica.Practica01.feature.medico.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository repository;

    @Override
    public List<SpecialityResponseDto> findAll() {
        return null;
    }

    @Override
    public SpecialityResponseDto findById(Long id) {
        return null;
    }

    @Override
    public SpecialityResponseDto create(SpecialityRequestDto request) {
        return null;
    }

    @Override
    public SpecialityResponseDto update(Long id, SpecialityRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
