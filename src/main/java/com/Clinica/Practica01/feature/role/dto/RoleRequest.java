package com.Clinica.Practica01.feature.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class RoleRequest {
    @NotBlank
    private String name;
    private String description;
    private Set<UUID> permissionIds = new HashSet<>();
}
