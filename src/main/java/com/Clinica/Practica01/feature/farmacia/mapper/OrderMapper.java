package com.Clinica.Practica01.feature.farmacia.mapper;

import com.Clinica.Practica01.feature.farmacia.dto.OrderRequestDto;
import com.Clinica.Practica01.feature.farmacia.dto.OrderResponseDto;
import com.Clinica.Practica01.feature.farmacia.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDto dto) {
        if (dto == null) return null;
        return Order.builder()
            // .field(dto.getField())
            .build();
    }

    public OrderResponseDto toDto(Order entity) {
        if (entity == null) return null;
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
