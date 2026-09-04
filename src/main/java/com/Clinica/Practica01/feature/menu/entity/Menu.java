package com.Clinica.Practica01.feature.menu.entity;

import com.Clinica.Practica01.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menus")
public class Menu extends BaseEntity {

    @Column(nullable = false)
    private String label;

    private String icon;

    private String route;

    @Column(name = "display_order")
    private Integer displayOrder;

    // Permiso que habilita ver este item (null = visible para cualquier autenticado)
    @Column(name = "required_permission")
    private String requiredPermission;

    // Jerarquia: item padre (null = raiz)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;
}
