package com.clinica.practica01.feature.permission.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionResponse extends BaseResponse {
    private String name;
    private String groupName;
    private String description;
}
