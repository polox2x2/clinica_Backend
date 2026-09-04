package com.Clinica.Practica01.feature.speciality.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class SpecialityRequest {
    @NotBlank
    private String name;
    private String description;
    private UUID parentId;
}
