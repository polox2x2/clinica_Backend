package com.clinica.practica01.feature.product.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductResponse extends BaseResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
