package br.com.training.inventory_control_system.adapter.in.controllers;

import br.com.training.inventory_control_system.adapter.in.requests.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.port.in.CategoryUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryUsecase useCase;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequest categoryRequest;
    private GetCategoryResponse getCategoryResponse;

    @BeforeEach
    void setUp() {
        categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName("Sample Category");

        getCategoryResponse = new GetCategoryResponse();
    }

    @Test
    void testSaveCategory() {
        doNothing().when(useCase).saveCategory(any(CategoryRequest.class));

        ResponseEntity<ApiResponse> response = categoryController.saveCategory(categoryRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The category named 'Sample Category' has been successfully created.", response.getBody().message());
        verify(useCase, times(1)).saveCategory(categoryRequest);
    }

    @Test
    void testGetCategory() {
        when(useCase.getCategory(1)).thenReturn(getCategoryResponse);

        ResponseEntity<GetCategoryResponse> response = categoryController.getCategory(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getCategoryResponse, response.getBody());
        verify(useCase, times(1)).getCategory(1);
    }

    @Test
    void testGetcategories() {
        List<GetCategoryResponse> CategoryList = Collections.singletonList(getCategoryResponse);
        when(useCase.getCategories()).thenReturn(CategoryList);

        ResponseEntity<List<GetCategoryResponse>> response = categoryController.getCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CategoryList, response.getBody());
        verify(useCase, times(1)).getCategories();
    }

    @Test
    void testUpdateCategory() {
        doNothing().when(useCase).updateCategory(any(Integer.class), any(CategoryRequest.class));

        ResponseEntity<ApiResponse> response = categoryController.updateCategory(1, categoryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Category with ID 1 was updated successfully.", response.getBody().message());
        verify(useCase, times(1)).updateCategory(1, categoryRequest);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(useCase).deleteCategory(1);

        assertDoesNotThrow(() -> {
            categoryController.deletarRegistro(1);
        });

        verify(useCase, times(1)).deleteCategory(1);
    }
}
