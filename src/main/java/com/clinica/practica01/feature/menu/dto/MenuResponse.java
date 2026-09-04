package com.clinica.practica01.feature.menu.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuResponse extends BaseResponse {
    private String label;
    private String icon;
    private String route;
    private Integer order;
    private String requiredPermission;
    private UUID parentId;
}
