package com.Clinica.Practica01.feature.product.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.Clinica.Practica01.feature.product.dto.ProductRequest;
import com.Clinica.Practica01.feature.product.dto.ProductResponse;
import com.Clinica.Practica01.feature.product.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Productos de farmacia")
public class ProductController extends BaseCrudController<ProductRequest, ProductResponse> {

    public ProductController(ProductService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "Product";
    }
}
