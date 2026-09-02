package com.Clinica.Practica01.feature.horario.service;

import com.Clinica.Practica01.feature.horario.dto.ScheduleResponseDto;
import com.Clinica.Practica01.feature.horario.dto.ScheduleRequestDto;
import com.Clinica.Practica01.feature.horario.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;

    @Override
    public List<ScheduleResponseDto> findAll() {
        return null;
    }

    @Override
    public ScheduleResponseDto findById(Long id) {
        return null;
    }

    @Override
    public ScheduleResponseDto create(ScheduleRequestDto request) {
        return null;
    }

    @Override
    public ScheduleResponseDto update(Long id, ScheduleRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
