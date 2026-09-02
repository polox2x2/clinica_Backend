package com.Clinica.Practica01.feature.farmacia.service;

import com.Clinica.Practica01.feature.farmacia.dto.OrderResponseDto;
import com.Clinica.Practica01.feature.farmacia.dto.OrderRequestDto;
import java.util.List;

public interface OrderService {
    List<OrderResponseDto> findAll();
    OrderResponseDto findById(Long id);
    OrderResponseDto create(OrderRequestDto request);
    OrderResponseDto update(Long id, OrderRequestDto request);
    void delete(Long id);
}
