package com.Clinica.Practica01.feature.usuario.service;

import com.Clinica.Practica01.feature.usuario.dto.UserResponseDto;
import com.Clinica.Practica01.feature.usuario.dto.UserRequestDto;
import com.Clinica.Practica01.feature.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<UserResponseDto> findAll() {
        return null;
    }

    @Override
    public UserResponseDto findById(Long id) {
        return null;
    }

    @Override
    public UserResponseDto create(UserRequestDto request) {
        return null;
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
