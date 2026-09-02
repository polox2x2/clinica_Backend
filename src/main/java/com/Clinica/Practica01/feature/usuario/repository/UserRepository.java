package com.Clinica.Practica01.feature.usuario.repository;

import com.Clinica.Practica01.feature.usuario.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
