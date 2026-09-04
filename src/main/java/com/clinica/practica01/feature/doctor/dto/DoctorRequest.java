package com.clinica.practica01.feature.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class DoctorRequest {
    // Datos de la cuenta (se crea el User con rol Medico)
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    // Requerido al crear; en update puede venir vacio para no cambiar la clave
    private String password;

    // Datos del medico
    @NotBlank
    private String cmp;
    private UUID specialityId;
}
