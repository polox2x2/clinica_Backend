package com.Clinica.Practica01.feature.paciente.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientResponseDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String documento;
    private LocalDate fechaNacimiento;
    private String telefono;
}
