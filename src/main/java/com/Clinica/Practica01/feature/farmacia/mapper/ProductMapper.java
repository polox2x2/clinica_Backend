package com.Clinica.Practica01.feature.farmacia.mapper;

import com.Clinica.Practica01.feature.farmacia.dto.ProductRequestDto;
import com.Clinica.Practica01.feature.farmacia.dto.ProductResponseDto;
import com.Clinica.Practica01.feature.farmacia.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto dto) {
        if (dto == null) return null;
        return Product.builder()
            // .field(dto.getField())
            .build();
    }

    public ProductResponseDto toDto(Product entity) {
        if (entity == null) return null;
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(entity.getId());
        // dto.setField(entity.getField());
        return dto;
    }
}
