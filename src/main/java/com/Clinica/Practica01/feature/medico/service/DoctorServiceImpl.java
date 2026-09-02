package com.Clinica.Practica01.feature.medico.service;

import com.Clinica.Practica01.feature.medico.dto.DoctorResponseDto;
import com.Clinica.Practica01.feature.medico.dto.DoctorRequestDto;
import com.Clinica.Practica01.feature.medico.dto.SpecialityDto;
import com.Clinica.Practica01.feature.medico.entity.Doctor;
import com.Clinica.Practica01.feature.medico.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;

    private DoctorResponseDto mapToDto(Doctor doctor) {
        DoctorResponseDto dto = new DoctorResponseDto();
        dto.setId(doctor.getId());
        dto.setNombre(doctor.getFirstName());
        dto.setApellido(doctor.getLastName());
        dto.setColegiatura(doctor.getCmp());
        if (doctor.getSpeciality() != null) {
            SpecialityDto spec = new SpecialityDto();
            spec.setId(doctor.getSpeciality().getId());
            spec.setNombre(doctor.getSpeciality().getName());
            dto.setEspecialidad(spec);
        }
        return dto;
    }

    @Override
    public List<DoctorResponseDto> findAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDto findById(Long id) {
        return repository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public DoctorResponseDto create(DoctorRequestDto request) { return null; }

    @Override
    public DoctorResponseDto update(Long id, DoctorRequestDto request) { return null; }

    @Override
    public void delete(Long id) { }
}
