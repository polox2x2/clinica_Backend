package com.clinica.practica01.feature.stockentry.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockEntryResponse extends BaseResponse {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitCost;
    private String note;
}
