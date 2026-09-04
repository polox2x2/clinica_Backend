package com.Clinica.Practica01.feature.role.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.role.dto.RoleRequest;
import com.Clinica.Practica01.feature.role.dto.RoleResponse;
import com.Clinica.Practica01.feature.role.entity.Role;
import com.Clinica.Practica01.feature.role.mapper.RoleMapper;
import com.Clinica.Practica01.feature.role.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl
        extends AbstractCrudService<Role, RoleRequest, RoleResponse>
        implements RoleService {

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String resourceName() {
        return "Role";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("name");
    }
}
