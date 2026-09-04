package com.Clinica.Practica01.feature.menu.dto;

import com.Clinica.Practica01.core.dto.BaseResponse;
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
