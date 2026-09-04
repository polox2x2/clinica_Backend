package com.clinica.practica01.core.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Verifica permisos granulares (formato "Entidad:Accion") contra las
 * authorities del usuario autenticado. Lo usa el CRUD generico.
 */
@Component
public class PermissionChecker {

    public boolean has(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (permission.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    /** Lanza 403 si el usuario no tiene el permiso. */
    public void require(String permission) {
        if (!has(permission)) {
            throw new AccessDeniedException("Falta el permiso: " + permission);
        }
    }
}
