package com.clinica.practica01.feature.stockentry.entity;

import com.clinica.practica01.core.domain.BaseEntity;
import com.clinica.practica01.feature.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/** Entrada de stock: registra ingreso de unidades de un producto al inventario. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stock_entries")
public class StockEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // Costo unitario de compra (opcional)
    @Column(precision = 12, scale = 2)
    private BigDecimal unitCost;

    private String note;
}
