package com.Clinica.Practica01.feature.product.mapper;

import com.Clinica.Practica01.core.mapper.BaseMapper;
import com.Clinica.Practica01.feature.product.dto.ProductRequest;
import com.Clinica.Practica01.feature.product.dto.ProductResponse;
import com.Clinica.Practica01.feature.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements BaseMapper<Product, ProductRequest, ProductResponse> {

    @Override
    public Product toEntity(ProductRequest r) {
        return Product.builder()
                .name(r.getName())
                .description(r.getDescription())
                .price(r.getPrice())
                .stock(r.getStock())
                .build();
    }

    @Override
    public void updateEntity(Product e, ProductRequest r) {
        e.setName(r.getName());
        e.setDescription(r.getDescription());
        e.setPrice(r.getPrice());
        // El stock no se edita directo aqui; se ajusta con entradas/ventas.
    }

    @Override
    public ProductResponse toResponse(Product e) {
        ProductResponse res = new ProductResponse();
        res.setName(e.getName());
        res.setDescription(e.getDescription());
        res.setPrice(e.getPrice());
        res.setStock(e.getStock());
        return res;
    }
}
