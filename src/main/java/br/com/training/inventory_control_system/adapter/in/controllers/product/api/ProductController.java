package br.com.training.inventory_control_system.adapter.in.controllers.product.api;

import br.com.training.inventory_control_system.adapter.in.controllers.product.request.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.port.in.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;

import java.util.List;

import static br.com.training.inventory_control_system.adapter.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/products", produces = APPLICATION_JSON_VALUE)
@Validated
public class ProductController implements ProductApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse> saveProduct(@RequestBody @Valid ProductRequest request) {
        LOGGER.info(LOG_PRODUCT_SAVE_OPERATION, request.getProductName());

        productService.saveProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .message(String.format("The product named '%s' has been successfully created.",
                                request.getProductName()))
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @Override
    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponse> getProduct(@PathVariable Integer productId) {
        LOGGER.info(LOG_PRODUCT_RECOVERY_OPERATION, productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProduct(productId));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<GetProductResponse>> getProducts() {
        LOGGER.info(LOG_PRODUCTS_RECOVERY_OPERATION);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProducts());
    }

    @Override
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductRequest request) {
        LOGGER.info(LOG_PRODUCT_UPDATE_OPERATION, productId);

        productService.updateProduct(productId, request);

        return ResponseEntity.ok(ApiResponse.builder()
                .message(String.format("Product with ID %s was updated successfully.", productId))
                .status(HttpStatus.OK.value())
                .build());
    }

    @Override
    @DeleteMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletarProduct(@PathVariable Integer productId) {
        LOGGER.info(LOG_PRODUCT_DELETE_OPERATION, productId);
        productService.deleteProduct(productId);
    }
}
