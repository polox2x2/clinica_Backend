package com.clinica.practica01.feature.patient.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/** Datos que el propio paciente completa de su perfil (su cuenta ya existe). */
@Data
public class SelfPatientRequest {
    @NotBlank
    private String documentId;
    private LocalDate dateOfBirth;
    private String phone;
}
