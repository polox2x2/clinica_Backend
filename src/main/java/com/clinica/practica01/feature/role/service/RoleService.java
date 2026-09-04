package com.clinica.practica01.feature.role.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.role.dto.RoleRequest;
import com.clinica.practica01.feature.role.dto.RoleResponse;

/** Contrato del servicio de Role. */
public interface RoleService extends CrudService<RoleRequest, RoleResponse> {
}
