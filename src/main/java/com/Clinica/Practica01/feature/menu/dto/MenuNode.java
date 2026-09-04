package com.Clinica.Practica01.feature.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Nodo del arbol de menu para pintar en la UI. */
@Data
@Builder
public class MenuNode {
    private UUID id;
    private String label;
    private String icon;
    private String route;
    private UUID parentId;
    private Integer order;
    @Builder.Default
    private List<MenuNode> children = new ArrayList<>();
}
