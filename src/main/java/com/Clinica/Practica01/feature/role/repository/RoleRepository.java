package com.Clinica.Practica01.feature.role.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.role.entity.Role;

import java.util.Optional;

public interface RoleRepository extends BaseRepository<Role> {
    Optional<Role> findByName(String name);
}
