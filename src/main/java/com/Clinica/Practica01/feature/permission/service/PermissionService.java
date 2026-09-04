package com.Clinica.Practica01.feature.permission.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.permission.dto.PermissionRequest;
import com.Clinica.Practica01.feature.permission.dto.PermissionResponse;

/** Contrato del servicio de Permission. */
public interface PermissionService extends CrudService<PermissionRequest, PermissionResponse> {
}
