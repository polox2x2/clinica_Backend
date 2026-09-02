package com.Clinica.Practica01.feature.usuario.service;

import com.Clinica.Practica01.feature.usuario.dto.UserResponseDto;
import com.Clinica.Practica01.feature.usuario.dto.UserRequestDto;
import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();
    UserResponseDto findById(Long id);
    UserResponseDto create(UserRequestDto request);
    UserResponseDto update(Long id, UserRequestDto request);
    void delete(Long id);
}
