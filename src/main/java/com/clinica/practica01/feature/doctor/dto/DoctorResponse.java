package com.clinica.practica01.feature.doctor.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorResponse extends BaseResponse {
    private String cmp;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UUID userId;
    private UUID specialityId;
    private String specialityName;
}
