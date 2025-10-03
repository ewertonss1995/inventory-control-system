package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.product.request.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.ProductMapper;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.category.CategoryNotFoundException;
import br.com.training.inventory_control_system.application.exception.product.ProductNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Category;
import br.com.training.inventory_control_system.domain.entities.Product;
import br.com.training.inventory_control_system.domain.repositories.CategoryRepository;
import br.com.training.inventory_control_system.domain.repositories.ProductRepository;
import br.com.training.inventory_control_system.port.in.ProductService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, CategoryRepository categoryRepository, ProductMapper mapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
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
                    String.format("Unable to get products: %s", e.getMessage()), 1);
        }
    }

    @Override
    @Transactional
    public void updateProduct(Integer productId, ProductRequest request) {
        try {
            Product entity = repository.findProductWithCategory(productId).
                    orElseThrow(() -> new ProductNotFoundException(
                            String.format("Product with ID %s was not found.", productId)));

            mapper.updateEntityFromRequest(request, entity);

            if (!entity.getCategory().getCategoryId().equals(request.getCategoryId())) {
                Category newCategory = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new CategoryNotFoundException(
                                String.format("Category with ID %s was not found.", request.getCategoryId())));
                entity.setCategory(newCategory);
            }

            entity.setTotalPrice(entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity())));
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
