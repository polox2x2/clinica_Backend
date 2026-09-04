package com.Clinica.Practica01.feature.role.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.Clinica.Practica01.feature.role.dto.RoleRequest;
import com.Clinica.Practica01.feature.role.dto.RoleResponse;
import com.Clinica.Practica01.feature.role.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role", description = "Roles dinamicos (combinan permisos)")
public class RoleController extends BaseCrudController<RoleRequest, RoleResponse> {

    public RoleController(RoleService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "Role";
    }
}
