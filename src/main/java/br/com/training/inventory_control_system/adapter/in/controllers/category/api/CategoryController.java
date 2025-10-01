package br.com.training.inventory_control_system.adapter.in.controllers.category.api;

import br.com.training.inventory_control_system.adapter.in.controllers.category.request.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.port.in.CategoryService;
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
@RequestMapping(value = "/api/v1/categories", produces = APPLICATION_JSON_VALUE)
@Validated
public class CategoryController implements CategoryApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse> saveCategory(@RequestBody @Valid CategoryRequest request) {
        LOGGER.info(LOG_CATEGORY_SAVE_OPERATION, request.getCategoryName());

        categoryService.saveCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .message(String.format("The category named '%s' has been successfully created.",
                                request.getCategoryName()))
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @Override
    @GetMapping("/{categoryId}")
    public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Integer categoryId) {
        LOGGER.info(LOG_CATEGORY_RECOVERY_OPERATION, categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategory(categoryId));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<GetCategoryResponse>> getCategories() {
        LOGGER.info(LOG_CATEGORIES_RECOVERY_OPERATION);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategories());
    }

    @Override
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Integer categoryId, @RequestBody @Valid CategoryRequest request) {
        LOGGER.info(LOG_CATEGORY_UPDATE_OPERATION, categoryId);

        categoryService.updateCategory(categoryId, request);

        return ResponseEntity.ok(ApiResponse.builder()
                .message(String.format("Category with ID %s was updated successfully.", categoryId))
                .status(HttpStatus.OK.value())
                .build());
    }

    @Override
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer categoryId) {
        LOGGER.info(LOG_CATEGORY_DELETE_OPERATION, categoryId);
        categoryService.deleteCategory(categoryId);
    }
}
