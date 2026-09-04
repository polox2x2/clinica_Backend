package com.clinica.practica01.feature.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

/** Datos de la sesion actual: identidad + roles + permisos. El menu se obtiene
 *  aparte via GET /api/menus/tree. */
@Data
@Builder
public class MeResponse {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
    private Set<String> permissions;
}
