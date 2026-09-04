package com.Clinica.Practica01.feature.product.entity;

import com.Clinica.Practica01.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    // Stock actual; se ajusta con entradas (suma) y ventas (resta)
    @Column(nullable = false)
    private Integer stock;
}
