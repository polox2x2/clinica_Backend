package com.Clinica.Practica01.feature.user.dto;

import com.Clinica.Practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends BaseResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<String> roles;
}
