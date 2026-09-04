package com.Clinica.Practica01.feature.permission.dto;

import com.Clinica.Practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionResponse extends BaseResponse {
    private String name;
    private String groupName;
    private String description;
}
