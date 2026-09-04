package com.clinica.practica01.feature.stockentry.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.product.entity.Product;
import com.clinica.practica01.feature.product.repository.ProductRepository;
import com.clinica.practica01.feature.stockentry.dto.StockEntryRequest;
import com.clinica.practica01.feature.stockentry.dto.StockEntryResponse;
import com.clinica.practica01.feature.stockentry.entity.StockEntry;
import com.clinica.practica01.feature.stockentry.mapper.StockEntryMapper;
import com.clinica.practica01.feature.stockentry.repository.StockEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockEntryServiceImpl
        extends AbstractCrudService<StockEntry, StockEntryRequest, StockEntryResponse>
        implements StockEntryService {

    private final ProductRepository productRepository;

    public StockEntryServiceImpl(StockEntryRepository repository, StockEntryMapper mapper,
                                 ProductRepository productRepository) {
        super(repository, mapper);
        this.productRepository = productRepository;
    }

    @Override
    protected String resourceName() {
        return "StockEntry";
    }

    /** Registra la entrada y suma las unidades al stock del producto. */
    @Override
    @Transactional
    public StockEntryResponse create(StockEntryRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .filter(Product::isActive)
                .orElseThrow(() -> new BusinessException("Producto no encontrado", HttpStatus.NOT_FOUND));

        product.setStock(product.getStock() + request.getQuantity());
        productRepository.save(product);

        StockEntry entry = mapper.toEntity(request);
        entry.setProduct(product);
        entry.setActive(true);
        return mapper.toResponseWithBase(repository.save(entry));
    }
}
