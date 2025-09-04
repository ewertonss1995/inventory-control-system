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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
        repository.save(mapper.toEntity(request));
    }

    @Override
    public GetProductResponse getProduct(Integer productId) {
        try {
            Product entity = repository.findById(productId).
                    orElseThrow(() -> new ProductNotFoundException(
                            String.format("Product %s does not exist in the database.", productId)));

            return mapper.toGetProductResponse(entity);

        } catch (DataAccessException e) {
            LOGGER.error("[ProductService] - Error getting product: {}", e.getMessage());
            throw e;
        } catch (ProductNotFoundException e) {
            LOGGER.error("[ProductService] - Product not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("[ProductService] - Unexpected error retrieving product: {}", e.getMessage());
            throw new ProductCustomException(
                    String.format("Unable to get product: %s", e.getMessage()), e);
        }
    }

    @Override
    public List<GetProductResponse> getProducts() {
        try {
            List<Product> entities = repository.findAll();

            return mapper.toGetProductResponseList(entities);

        } catch (DataAccessException e) {
            LOGGER.error("[ProductService] - Error getting products: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            LOGGER.error("[ProductService] - Unexpected error retrieving products: {}", e.getMessage());
            throw new ProductCustomException(
                    String.format("Unable to get products: %s", e.getMessage()), e);
        }
    }
}
