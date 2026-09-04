package com.clinica.practica01.feature.permission.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.permission.dto.PermissionRequest;
import com.clinica.practica01.feature.permission.dto.PermissionResponse;

/** Contrato del servicio de Permission. */
public interface PermissionService extends CrudService<PermissionRequest, PermissionResponse> {
}
