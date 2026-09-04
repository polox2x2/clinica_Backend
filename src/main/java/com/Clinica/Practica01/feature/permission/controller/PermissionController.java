package com.Clinica.Practica01.feature.permission.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.Clinica.Practica01.feature.permission.dto.PermissionRequest;
import com.Clinica.Practica01.feature.permission.dto.PermissionResponse;
import com.Clinica.Practica01.feature.permission.service.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "Permission", description = "Catalogo de permisos Entidad:Accion")
public class PermissionController extends BaseCrudController<PermissionRequest, PermissionResponse> {

    public PermissionController(PermissionService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "Permission";
    }
}
