package com.clinica.practica01.feature.permission.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionRequest {
    @NotBlank
    private String name;
    private String groupName;
    private String description;
}
