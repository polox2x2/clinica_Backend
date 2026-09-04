package com.Clinica.Practica01.feature.role.mapper;

import com.Clinica.Practica01.core.mapper.BaseMapper;
import com.Clinica.Practica01.feature.permission.entity.Permission;
import com.Clinica.Practica01.feature.permission.mapper.PermissionMapper;
import com.Clinica.Practica01.feature.permission.repository.PermissionRepository;
import com.Clinica.Practica01.feature.role.dto.RoleRequest;
import com.Clinica.Practica01.feature.role.dto.RoleResponse;
import com.Clinica.Practica01.feature.role.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleMapper implements BaseMapper<Role, RoleRequest, RoleResponse> {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public Role toEntity(RoleRequest r) {
        Role role = Role.builder()
                .name(r.getName())
                .description(r.getDescription())
                .build();
        role.setPermissions(resolve(r.getPermissionIds()));
        return role;
    }

    @Override
    public void updateEntity(Role e, RoleRequest r) {
        e.setName(r.getName());
        e.setDescription(r.getDescription());
        e.setPermissions(resolve(r.getPermissionIds()));
    }

    @Override
    public RoleResponse toResponse(Role e) {
        RoleResponse res = new RoleResponse();
        res.setName(e.getName());
        res.setDescription(e.getDescription());
        List<com.Clinica.Practica01.feature.permission.dto.PermissionResponse> perms =
                e.getPermissions().stream().map(permissionMapper::toResponseWithBase).toList();
        res.setPermissions(perms);
        return res;
    }

    private Set<Permission> resolve(Set<UUID> ids) {
        Set<Permission> set = new HashSet<>();
        if (ids != null) {
            ids.forEach(id -> permissionRepository.findById(id).ifPresent(set::add));
        }
        return set;
    }
}
