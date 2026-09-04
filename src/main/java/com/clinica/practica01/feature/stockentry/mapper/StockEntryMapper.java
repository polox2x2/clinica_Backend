package com.clinica.practica01.feature.stockentry.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.product.repository.ProductRepository;
import com.clinica.practica01.feature.stockentry.dto.StockEntryRequest;
import com.clinica.practica01.feature.stockentry.dto.StockEntryResponse;
import com.clinica.practica01.feature.stockentry.entity.StockEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockEntryMapper implements BaseMapper<StockEntry, StockEntryRequest, StockEntryResponse> {

    private final ProductRepository productRepository;

    @Override
    public StockEntry toEntity(StockEntryRequest r) {
        StockEntry e = StockEntry.builder()
                .quantity(r.getQuantity())
                .unitCost(r.getUnitCost())
                .note(r.getNote())
                .build();
        e.setProduct(productRepository.findById(r.getProductId()).orElse(null));
        return e;
    }

    @Override
    public void updateEntity(StockEntry e, StockEntryRequest r) {
        // Las entradas no se editan (mantienen la trazabilidad del inventario).
    }

    @Override
    public StockEntryResponse toResponse(StockEntry e) {
        StockEntryResponse res = new StockEntryResponse();
        res.setQuantity(e.getQuantity());
        res.setUnitCost(e.getUnitCost());
        res.setNote(e.getNote());
        if (e.getProduct() != null) {
            res.setProductId(e.getProduct().getId());
            res.setProductName(e.getProduct().getName());
        }
        return res;
    }
}
