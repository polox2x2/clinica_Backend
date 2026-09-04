package com.Clinica.Practica01.feature.permission.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.permission.dto.PermissionRequest;
import com.Clinica.Practica01.feature.permission.dto.PermissionResponse;
import com.Clinica.Practica01.feature.permission.entity.Permission;
import com.Clinica.Practica01.feature.permission.mapper.PermissionMapper;
import com.Clinica.Practica01.feature.permission.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl
        extends AbstractCrudService<Permission, PermissionRequest, PermissionResponse>
        implements PermissionService {

    public PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String resourceName() {
        return "Permission";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("name", "groupName");
    }
}
