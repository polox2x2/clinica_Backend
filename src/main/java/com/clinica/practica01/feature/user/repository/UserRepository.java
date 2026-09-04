package com.clinica.practica01.feature.user.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.user.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
