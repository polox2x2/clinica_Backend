package com.Clinica.Practica01.feature.usuario.mapper;

import com.Clinica.Practica01.feature.usuario.dto.UserRequestDto;
import com.Clinica.Practica01.feature.usuario.dto.UserResponseDto;
import com.Clinica.Practica01.feature.usuario.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto dto) {
        if (dto == null) return null;
        return User.builder()
            // .field(dto.getField())
            .build();
    }

    public UserResponseDto toDto(User entity) {
        if (entity == null) return null;
        UserResponseDto dto = new UserResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
