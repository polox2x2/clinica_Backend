package com.Clinica.Practica01.feature.permission.entity;

import com.Clinica.Practica01.core.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Permiso granular del catalogo fijo, formato "Entidad:Accion" (ej. Doctor:Create).
 * Los permisos se siembran; el admin no los crea, solo los agrupa en roles.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    // La authority: "Doctor:Create", "Patient:List", etc.
    @Column(nullable = false, unique = true)
    private String name;

    // Grupo para la UI (la entidad: "Doctor", "Patient"...)
    @Column(name = "group_name")
    private String groupName;

    private String description;
}
