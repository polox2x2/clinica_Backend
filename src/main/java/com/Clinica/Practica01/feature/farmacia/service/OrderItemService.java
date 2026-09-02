package com.Clinica.Practica01.feature.farmacia.service;

import com.Clinica.Practica01.feature.farmacia.dto.OrderItemResponseDto;
import com.Clinica.Practica01.feature.farmacia.dto.OrderItemRequestDto;
import java.util.List;

public interface OrderItemService {
    List<OrderItemResponseDto> findAll();
    OrderItemResponseDto findById(Long id);
    OrderItemResponseDto create(OrderItemRequestDto request);
    OrderItemResponseDto update(Long id, OrderItemRequestDto request);
    void delete(Long id);
}
