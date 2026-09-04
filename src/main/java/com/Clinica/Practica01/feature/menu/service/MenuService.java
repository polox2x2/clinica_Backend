package com.Clinica.Practica01.feature.menu.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.menu.dto.MenuNode;
import com.Clinica.Practica01.feature.menu.dto.MenuRequest;
import com.Clinica.Practica01.feature.menu.dto.MenuResponse;

import java.util.List;
import java.util.Set;

/** Contrato del servicio de Menu (CRUD + arbol filtrado por permisos). */
public interface MenuService extends CrudService<MenuRequest, MenuResponse> {

    /** Arbol de menu visible para el conjunto de permisos dado. */
    List<MenuNode> getTree(Set<String> permissions);
}
