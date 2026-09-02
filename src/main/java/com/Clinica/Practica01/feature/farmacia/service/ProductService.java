package com.Clinica.Practica01.feature.farmacia.service;

import com.Clinica.Practica01.feature.farmacia.dto.ProductResponseDto;
import com.Clinica.Practica01.feature.farmacia.dto.ProductRequestDto;
import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();
    ProductResponseDto findById(Long id);
    ProductResponseDto create(ProductRequestDto request);
    ProductResponseDto update(Long id, ProductRequestDto request);
    void delete(Long id);
}
