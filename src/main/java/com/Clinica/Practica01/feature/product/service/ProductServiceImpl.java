package com.Clinica.Practica01.feature.product.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.product.dto.ProductRequest;
import com.Clinica.Practica01.feature.product.dto.ProductResponse;
import com.Clinica.Practica01.feature.product.entity.Product;
import com.Clinica.Practica01.feature.product.mapper.ProductMapper;
import com.Clinica.Practica01.feature.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl
        extends AbstractCrudService<Product, ProductRequest, ProductResponse>
        implements ProductService {

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String resourceName() {
        return "Product";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("name", "description");
    }
}
