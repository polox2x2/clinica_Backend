package com.Clinica.Practica01.feature.paciente.service;

import com.Clinica.Practica01.feature.paciente.dto.PatientResponseDto;
import com.Clinica.Practica01.feature.paciente.dto.PatientRequestDto;
import com.Clinica.Practica01.feature.paciente.entity.Patient;
import com.Clinica.Practica01.feature.paciente.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    private PatientResponseDto mapToDto(Patient patient) {
        PatientResponseDto dto = new PatientResponseDto();
        dto.setId(patient.getId());
        dto.setNombre(patient.getFirstName());
        dto.setApellido(patient.getLastName());
        dto.setDocumento(patient.getDocumentId());
        dto.setFechaNacimiento(patient.getDateOfBirth());
        dto.setTelefono(patient.getPhone());
        return dto;
    }

    @Override
    public List<PatientResponseDto> findAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public PatientResponseDto findById(Long id) {
        return repository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public PatientResponseDto create(PatientRequestDto request) {
        Patient patient = new Patient();
        patient.setFirstName(request.getNombre());
        patient.setLastName(request.getApellido());
        patient.setDocumentId(request.getDocumento());
        patient.setPhone(request.getTelefono());
        
        if(request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
            patient.setDateOfBirth(java.time.LocalDate.parse(request.getFechaNacimiento()));
        }

        Patient saved = repository.save(patient);
        return mapToDto(saved);
    }

    @Override
    public PatientResponseDto update(Long id, PatientRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
