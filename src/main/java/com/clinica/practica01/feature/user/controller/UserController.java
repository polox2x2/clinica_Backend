package com.clinica.practica01.feature.user.controller;

import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.clinica.practica01.feature.user.dto.UserRequest;
import com.clinica.practica01.feature.user.dto.UserResponse;
import com.clinica.practica01.feature.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Usuarios del sistema y sus roles")
public class UserController extends BaseCrudController<UserRequest, UserResponse> {

    public UserController(UserService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "User";
    }
}
