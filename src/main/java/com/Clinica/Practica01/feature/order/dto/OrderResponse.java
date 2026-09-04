package com.Clinica.Practica01.feature.order.dto;

import com.Clinica.Practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderResponse extends BaseResponse {
    private UUID patientId;
    private String patientName;
    private BigDecimal total;
    private List<OrderItemResponse> items;
}
