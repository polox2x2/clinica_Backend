package com.clinica.practica01.feature.role.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import com.clinica.practica01.feature.permission.dto.PermissionResponse;
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
