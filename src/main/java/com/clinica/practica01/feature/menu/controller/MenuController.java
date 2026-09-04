package com.clinica.practica01.feature.menu.controller;

import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.core.web.BaseCrudController;
import com.clinica.practica01.feature.menu.dto.MenuNode;
import com.clinica.practica01.feature.menu.dto.MenuRequest;
import com.clinica.practica01.feature.menu.dto.MenuResponse;
import com.clinica.practica01.feature.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
@Tag(name = "Menu", description = "Menu dinamico jerarquico; el arbol se filtra por los permisos del usuario")
public class MenuController extends BaseCrudController<MenuRequest, MenuResponse> {

    private final MenuService menuService;

    public MenuController(MenuService service, PermissionChecker permissions) {
        super(service, permissions);
        this.menuService = service;
    }

    @Override
    protected String permissionPrefix() {
        return "Menu";
    }

    @Operation(summary = "Arbol de menu del usuario",
            description = "Devuelve el menu jerarquico (parent/children) filtrado por los permisos del "
                    + "usuario autenticado. Lo consume el frontend para pintar la navegacion.")
    @GetMapping("/tree")
    public ResponseEntity<List<MenuNode>> tree(Authentication authentication) {
        Set<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(menuService.getTree(permissions));
    }
}
