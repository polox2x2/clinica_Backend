package com.clinica.practica01.feature.menu.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.menu.dto.MenuNode;
import com.clinica.practica01.feature.menu.dto.MenuRequest;
import com.clinica.practica01.feature.menu.dto.MenuResponse;

import java.util.List;
import java.util.Set;

/** Contrato del servicio de Menu (CRUD + arbol filtrado por permisos). */
public interface MenuService extends CrudService<MenuRequest, MenuResponse> {

    /** Arbol de menu visible para el conjunto de permisos dado. */
    List<MenuNode> getTree(Set<String> permissions);
}
