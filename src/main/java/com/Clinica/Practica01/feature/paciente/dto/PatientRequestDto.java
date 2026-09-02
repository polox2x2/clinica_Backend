package com.Clinica.Practica01.feature.paciente.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PatientRequestDto {
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;
    private String fechaNacimiento;
}
