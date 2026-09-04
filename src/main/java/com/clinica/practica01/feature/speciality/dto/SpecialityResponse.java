package com.clinica.practica01.feature.speciality.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialityResponse extends BaseResponse {
    private String name;
    private String description;
    private UUID parentId;
    private String parentName;
}
