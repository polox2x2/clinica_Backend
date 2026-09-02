package com.Clinica.Practica01.feature.farmacia.service;

import com.Clinica.Practica01.feature.farmacia.dto.ProductResponseDto;
import com.Clinica.Practica01.feature.farmacia.dto.ProductRequestDto;
import com.Clinica.Practica01.feature.farmacia.entity.Product;
import com.Clinica.Practica01.feature.farmacia.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private ProductResponseDto mapToDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setNombre(product.getName());
        dto.setDescripcion(product.getDescription());
        dto.setPrecio(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto findById(Long id) {
        return repository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public ProductResponseDto create(ProductRequestDto request) { return null; }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto request) { return null; }

    @Override
    public void delete(Long id) { }
}
