package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.ProductMapper;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.product.ProductNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Product;
import br.com.training.inventory_control_system.domain.repositories.ProductRepository;
import br.com.training.inventory_control_system.port.in.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveProduct(ProductRequest request) {
        try {
            Product entity = mapper.toEntity(request);
            repository.save(entity);
        } catch (Exception e) {
            LOGGER.error("[ProductServiceImpl] - Unexpected error posting product: {}", e.getMessage());
            throw new GeneralCustomException(
                    String.format("Unable to save product: %s", e.getMessage()), e);
        }
    }

    @Override
    public GetProductResponse getProduct(Integer productId) {
        Product entity = repository.findProductWithCategory(productId).
                orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product with ID %s was not found.", productId)));

        return mapper.toGetProductResponse(entity);
    }

    @Override
    public List<GetProductResponse> getProducts() {
        try {
            List<Product> entities = repository.findAll();
            return mapper.toGetProductResponseList(entities);

        } catch (Exception e) {
            LOGGER.error("[ProductServiceImpl] - Unexpected error retrieving products: {}", e.getMessage());
            throw new EmptyResultDataAccessException(
                    String.format("Unable to update products: %s", e.getMessage()), 1);
        }
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest request) {
        try {
            Product entity = repository.findById(productId).
                    orElseThrow(() -> new ProductNotFoundException(
                            String.format("Product with ID %s was not found.", productId)));

            mapper.updateEntityFromRequest(request, entity);

            entity.setUpdateDate(LocalDateTime.now());
            repository.save(entity);

        } catch (Exception e) {
            LOGGER.error("[ProductServiceImpl] - Unexpected error updating products: {}", e.getMessage());
            throw new GeneralCustomException(
                    String.format("Error updating product: %s", e.getMessage()), e);
        }

        LOGGER.info("[ProductServiceImpl] - Product with ID: {} was updated successfully.", productId);
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product entity = repository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product with ID %s was not found.", productId)));

        repository.delete(entity);

        LOGGER.info("[ProductServiceImpl] - Product with ID: {} was deleted successfully.", productId);
    }
}
