package com.Clinica.Practica01.feature.cita.service;

import com.Clinica.Practica01.feature.cita.dto.AppointmentResponseDto;
import com.Clinica.Practica01.feature.cita.dto.AppointmentRequestDto;
import com.Clinica.Practica01.feature.cita.entity.Appointment;
import com.Clinica.Practica01.feature.cita.repository.AppointmentRepository;
import com.Clinica.Practica01.feature.paciente.dto.PatientResponseDto;
import com.Clinica.Practica01.feature.medico.dto.DoctorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;

    private AppointmentResponseDto mapToDto(Appointment appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setEstado(appointment.getStatus().name());
        
        if (appointment.getSchedule() != null && appointment.getSchedule().getDoctor() != null) {
            DoctorResponseDto doc = new DoctorResponseDto();
            doc.setNombre(appointment.getSchedule().getDoctor().getFirstName());
            doc.setApellido(appointment.getSchedule().getDoctor().getLastName());
            dto.setMedico(doc);
            
            // Simular fecha de la cita con el dia del horario para visualización
            dto.setFecha(appointment.getSchedule().getAvailableDate() + " " + appointment.getSchedule().getStartTime());
        }

        if (appointment.getPatient() != null) {
            PatientResponseDto pat = new PatientResponseDto();
            pat.setId(appointment.getPatient().getId());
            pat.setNombre(appointment.getPatient().getFirstName());
            pat.setApellido(appointment.getPatient().getLastName());
            dto.setPaciente(pat);
        }
        
        return dto;
    }

    @Override
    public List<AppointmentResponseDto> findAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto findById(Long id) {
        return repository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public AppointmentResponseDto create(AppointmentRequestDto request) { return null; }

    @Override
    public AppointmentResponseDto update(Long id, AppointmentRequestDto request) { return null; }

    @Override
    public void delete(Long id) { }
}
