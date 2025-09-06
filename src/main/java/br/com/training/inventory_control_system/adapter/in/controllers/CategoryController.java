package br.com.training.inventory_control_system.adapter.in.controllers;

import br.com.training.inventory_control_system.adapter.in.requests.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.port.in.CategoryUsecase;
import jakarta.validation.Valid;
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

import java.util.List;

import static br.com.training.inventory_control_system.adapter.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/categories", produces = APPLICATION_JSON_VALUE)
@Validated
public class CategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryUsecase useCase;

    public CategoryController(CategoryUsecase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveCategory(@RequestBody @Valid CategoryRequest request) {
        LOGGER.info(LOG_CATEGORY_SAVE_OPERATION, request.getCategoryName());

        useCase.saveCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .message(String.format("The category named '%s' has been successfully created.",
                                request.getCategoryName()))
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @GetMapping("/{CategoryId}")
    public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Integer CategoryId) {
        LOGGER.info(LOG_CATEGORY_RECOVERY_OPERATION, CategoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(useCase.getCategory(CategoryId));
    }

    @GetMapping
    public ResponseEntity<List<GetCategoryResponse>> getCategories() {
        LOGGER.info(LOG_CATEGORIES_RECOVERY_OPERATION);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(useCase.getCategories());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Integer categoryId, @RequestBody @Valid CategoryRequest request) {
        LOGGER.info(LOG_CATEGORY_UPDATE_OPERATION, categoryId);

        useCase.updateCategory(categoryId, request);

        return ResponseEntity.ok(ApiResponse.builder()
                .message(String.format("Category with ID %s was updated successfully.", categoryId))
                .status(HttpStatus.OK.value())
                .build());
    }

    @DeleteMapping("/{CategoryId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletarRegistro(@PathVariable Integer CategoryId) {
        LOGGER.info(LOG_CATEGORY_DELETE_OPERATION, CategoryId);
        useCase.deleteCategory(CategoryId);
    }
}
