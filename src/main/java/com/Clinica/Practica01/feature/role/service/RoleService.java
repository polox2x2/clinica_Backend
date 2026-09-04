package com.Clinica.Practica01.feature.role.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.role.dto.RoleRequest;
import com.Clinica.Practica01.feature.role.dto.RoleResponse;

/** Contrato del servicio de Role. */
public interface RoleService extends CrudService<RoleRequest, RoleResponse> {
}
