package com.Clinica.Practica01.feature.farmacia.mapper;

import com.Clinica.Practica01.feature.farmacia.dto.OrderItemRequestDto;
import com.Clinica.Practica01.feature.farmacia.dto.OrderItemResponseDto;
import com.Clinica.Practica01.feature.farmacia.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItem toEntity(OrderItemRequestDto dto) {
        if (dto == null) return null;
        return OrderItem.builder()
            // .field(dto.getField())
            .build();
    }

    public OrderItemResponseDto toDto(OrderItem entity) {
        if (entity == null) return null;
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
