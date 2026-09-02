package com.Clinica.Practica01.feature.cita.dto;

import com.Clinica.Practica01.feature.paciente.dto.PatientResponseDto;
import com.Clinica.Practica01.feature.medico.dto.DoctorResponseDto;
import lombok.Data;

@Data
public class AppointmentResponseDto {
    private Long id;
    private String fecha;
    private String estado;
    private PatientResponseDto paciente;
    private DoctorResponseDto medico;
}
