package br.com.training.inventory_control_system.adapter.in.controllers;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.ProductResponse;
import br.com.training.inventory_control_system.port.in.ProductUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/products", produces = APPLICATION_JSON_VALUE)
@Validated
public class ProductController {

    private final ProductUsecase useCase;

    public ProductController(ProductUsecase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> saveProduct(@RequestBody @Valid ProductRequest request) {

        useCase.saveProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProductResponse(
                                String.format("The product named '%s' has been successfully created.",
                                        request.getProductName()),
                                HttpStatus.CREATED.value()));
    }
}
