package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.ProductMapper;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.application.exception.ProductCustomException;
import br.com.training.inventory_control_system.application.exception.ProductNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Product;
import br.com.training.inventory_control_system.domain.repositories.ProductRepository;
import br.com.training.inventory_control_system.port.in.ProductUsecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService implements ProductUsecase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveProduct(ProductRequest request) {
        try {
            repository.save(mapper.toEntity(request));
        } catch (Exception e) {
            LOGGER.error("[ProductService] - Unexpected error posting product: {}", e.getMessage());
            throw new ProductCustomException(
                    String.format("Unable to update product: %s", e.getMessage()), e);
        }
    }

    @Override
    public GetProductResponse getProduct(Integer productId) {
        Product entity = repository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(
                        String.format("Unable to get product with ID: %s", productId)));

        return mapper.toGetProductResponse(entity);
    }

    @Override
    public List<GetProductResponse> getProducts() {
        try {
            List<Product> entities = repository.findAll();
            return mapper.toGetProductResponseList(entities);

        } catch (Exception e) {
            LOGGER.error("[ProductService] - Unexpected error retrieving products: {}", e.getMessage());
            throw new ProductCustomException(
                    String.format("Unable to get products: %s", e.getMessage()), e);
        }
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest request) {
        try {
            Product entity = repository.findById(productId).
                    orElseThrow(() -> new ProductNotFoundException(
                            String.format("Unable to get product with ID %s for update.", productId)));

            mapper.updateEntityFromRequest(request, entity);

            entity.setUpdateDate(LocalDateTime.now());
            repository.save(entity);

        } catch (Exception e) {
            LOGGER.error("[ProductService] - Unexpected error updating products: {}", e.getMessage());
            throw new ProductCustomException(
                    String.format("Unable to update product: %s", e.getMessage()), e);
        }

        LOGGER.info("[ProductService] - Product with ID: {} was updated successfully.", productId);
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product entity = repository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(
                        String.format("Unable to get product with ID %s for update.", productId)));

        repository.delete(entity);

        LOGGER.info("[ProductService] - Product with ID: {} was deleted successfully.", productId);
    }
}
