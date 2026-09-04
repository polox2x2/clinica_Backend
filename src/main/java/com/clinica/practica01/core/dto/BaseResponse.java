package com.clinica.practica01.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Campos comunes a toda respuesta de entidad. Los ResponseDto de cada feature
 * heredan de aqui para exponer id/isActive/createdAt de forma uniforme.
 */
@Data
public abstract class BaseResponse {
    private UUID id;

    @JsonProperty("isActive")
    private boolean active;

    private LocalDateTime createdAt;
}
