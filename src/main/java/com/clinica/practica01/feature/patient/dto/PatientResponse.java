package com.clinica.practica01.feature.patient.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientResponse extends BaseResponse {
    private String documentId;
    private LocalDate dateOfBirth;
    private String phone;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UUID userId;
}
