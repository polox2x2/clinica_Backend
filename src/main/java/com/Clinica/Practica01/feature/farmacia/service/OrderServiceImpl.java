package com.Clinica.Practica01.feature.farmacia.service;

import com.Clinica.Practica01.feature.farmacia.dto.OrderResponseDto;
import com.Clinica.Practica01.feature.farmacia.dto.OrderRequestDto;
import com.Clinica.Practica01.feature.farmacia.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    public List<OrderResponseDto> findAll() {
        return null;
    }

    @Override
    public OrderResponseDto findById(Long id) {
        return null;
    }

    @Override
    public OrderResponseDto create(OrderRequestDto request) {
        return null;
    }

    @Override
    public OrderResponseDto update(Long id, OrderRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
