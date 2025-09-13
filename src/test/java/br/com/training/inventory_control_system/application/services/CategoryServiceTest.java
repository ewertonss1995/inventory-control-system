package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.requests.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.CategoryMapper;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.category.CategoryNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Category;
import br.com.training.inventory_control_system.domain.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequest categoryRequest;
    private Category category;
    private GetCategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        categoryRequest = new CategoryRequest();
        category = new Category();
        categoryResponse = new GetCategoryResponse();
    }

    @Test
    void testSaveCategory() {
        when(mapper.toEntity(categoryRequest)).thenReturn(category);
        categoryService.saveCategory(categoryRequest);
        verify(repository, times(1)).save(category);
    }

    @Test
    void testSaveCategoryThrowsException() {
        when(mapper.toEntity(categoryRequest)).thenReturn(category);
        doThrow(new RuntimeException("Error")).when(repository).save(any(Category.class));

        GeneralCustomException exception = assertThrows(GeneralCustomException.class, () -> {
            categoryService.saveCategory(categoryRequest);
        });

        assertEquals("Unable to save category: Error", exception.getMessage());
        verify(repository, times(1)).save(category);
    }

    @Test
    void testGetCategory() {
        when(repository.findById(1)).thenReturn(Optional.of(category));
        when(mapper.toGetCategoryResponse(category)).thenReturn(categoryResponse);

        GetCategoryResponse response = categoryService.getCategory(1);

        assertEquals(categoryResponse, response);
        verify(repository, times(1)).findById(1);
        verify(mapper, times(1)).toGetCategoryResponse(category);
    }

    @Test
    void testGetCategoryThrowsNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getCategory(1);
        });

        assertEquals("Category with ID: 1 was not found.", exception.getMessage());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetCategorys() {
        when(repository.findAll()).thenReturn(Collections.singletonList(category));
        when(mapper.toGetCategoryResponseList(anyList())).thenReturn(Collections.singletonList(categoryResponse));

        List<GetCategoryResponse> response = categoryService.getCategories();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(categoryResponse, response.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetCategorysThrowsException() {
        when(repository.findAll()).thenThrow(new EmptyResultDataAccessException("Error", 1));

        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class, () -> {
            categoryService.getCategories();
        });

        assertEquals("Unable to get categories: Error", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory() {
        when(repository.findById(1)).thenReturn(Optional.of(category));
        doNothing().when(mapper).updateEntityFromRequest(categoryRequest, category);
        when(repository.save(category)).thenReturn(category);

        categoryService.updateCategory(1, categoryRequest);

        verify(repository, times(1)).findById(1);
        verify(mapper, times(1)).updateEntityFromRequest(categoryRequest, category);
        verify(repository, times(1)).save(category);
    }

    @Test
    void testUpdateCategoryThrowsNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        GeneralCustomException exception = assertThrows(GeneralCustomException.class, () -> {
            categoryService.updateCategory(1, categoryRequest);
        });

        assertEquals("Unable to update category: Category with ID 1 was not found.", exception.getMessage());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testDeleteCategory() {
        when(repository.findById(1)).thenReturn(Optional.of(category));
        doNothing().when(repository).delete(category);

        categoryService.deleteCategory(1);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(category);
    }

    @Test
    void testDeleteCategoryThrowsNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(1);
        });

        assertEquals("Category with ID 1 was not found.", exception.getMessage());
        verify(repository, times(1)).findById(1);
    }
}
