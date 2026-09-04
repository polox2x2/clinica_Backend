package com.clinica.practica01.feature.permission.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.permission.dto.PermissionRequest;
import com.clinica.practica01.feature.permission.dto.PermissionResponse;
import com.clinica.practica01.feature.permission.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper implements BaseMapper<Permission, PermissionRequest, PermissionResponse> {

    @Override
    public Permission toEntity(PermissionRequest r) {
        return Permission.builder()
                .name(r.getName())
                .groupName(r.getGroupName())
                .description(r.getDescription())
                .build();
    }

    @Override
    public void updateEntity(Permission e, PermissionRequest r) {
        e.setName(r.getName());
        e.setGroupName(r.getGroupName());
        e.setDescription(r.getDescription());
    }

    @Override
    public PermissionResponse toResponse(Permission e) {
        PermissionResponse res = new PermissionResponse();
        res.setName(e.getName());
        res.setGroupName(e.getGroupName());
        res.setDescription(e.getDescription());
        return res;
    }
}
