package com.Clinica.Practica01.feature.permission.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.permission.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends BaseRepository<Permission> {
    Optional<Permission> findByName(String name);
}
