package com.clinica.practica01.feature.menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class MenuRequest {
    @NotBlank
    private String label;
    private String icon;
    private String route;
    private Integer order;
    private String requiredPermission;
    private UUID parentId;
}
