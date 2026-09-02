package com.Clinica.Practica01.feature.farmacia.service;

import com.Clinica.Practica01.feature.farmacia.dto.OrderItemResponseDto;
import com.Clinica.Practica01.feature.farmacia.dto.OrderItemRequestDto;
import com.Clinica.Practica01.feature.farmacia.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository repository;

    @Override
    public List<OrderItemResponseDto> findAll() {
        return null;
    }

    @Override
    public OrderItemResponseDto findById(Long id) {
        return null;
    }

    @Override
    public OrderItemResponseDto create(OrderItemRequestDto request) {
        return null;
    }

    @Override
    public OrderItemResponseDto update(Long id, OrderItemRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
