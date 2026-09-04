package com.Clinica.Practica01.feature.role.dto;

import com.Clinica.Practica01.core.dto.BaseResponse;
import com.Clinica.Practica01.feature.permission.dto.PermissionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleResponse extends BaseResponse {
    private String name;
    private String description;
    private List<PermissionResponse> permissions;
}
