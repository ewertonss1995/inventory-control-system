package br.com.training.inventory_control_system.adapter.in.controllers;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.adapter.out.responses.ProductResponse;
import br.com.training.inventory_control_system.port.in.ProductUsecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/products", produces = APPLICATION_JSON_VALUE)
@Validated
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    public static final String LOG_PRODUCT_SAVE_OPERATION = "[ProductController] - Operation received to save product: {}";
    public static final String LOG_PRODUCT_RECOVERY_OPERATION = "[ProductController] - Operation received to recovery product with ID: {}";
    public static final String LOG_PRODUCTS_RECOVERY_OPERATION = "[ProductController] - Operation received to recovery products";
    public static final String LOG_PRODUCT_UPDATE_OPERATION = "[ProductController] - Operation received to update product with ID: {}";
    public static final String LOG_PRODUCT_DELETE_OPERATION = "[ProductController] - Operation received to delete product with ID: {}";

    private final ProductUsecase useCase;

    public ProductController(ProductUsecase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> saveProduct(@RequestBody @Valid ProductRequest request) {
        LOGGER.info(LOG_PRODUCT_SAVE_OPERATION, request.getProductName());

        useCase.saveProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductResponse.builder()
                        .message(String.format("The product named '%s' has been successfully created.",
                                request.getProductName()))
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponse> getProduct(@PathVariable Integer productId) {
        LOGGER.info(LOG_PRODUCT_RECOVERY_OPERATION, productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(useCase.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<List<GetProductResponse>> getProducts() {
        LOGGER.info(LOG_PRODUCTS_RECOVERY_OPERATION);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(useCase.getProducts());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductRequest request) {
        LOGGER.info(LOG_PRODUCT_UPDATE_OPERATION, productId);

        useCase.updateProduct(productId, request);

        return ResponseEntity.ok(ProductResponse.builder()
                .message(String.format("Product with ID %s was updated successfully.", productId))
                .status(HttpStatus.OK.value())
                .build());
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletarRegistro(@PathVariable Integer productId) {
        LOGGER.info(LOG_PRODUCT_DELETE_OPERATION, productId);
        useCase.deleteProduct(productId);
    }
}
