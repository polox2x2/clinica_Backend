package com.clinica.practica01.feature.permission.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.permission.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends BaseRepository<Permission> {
    Optional<Permission> findByName(String name);
}
