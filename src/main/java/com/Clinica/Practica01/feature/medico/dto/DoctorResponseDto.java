package com.Clinica.Practica01.feature.medico.dto;

import lombok.Data;

@Data
public class DoctorResponseDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String colegiatura;
    private SpecialityDto especialidad;
}
