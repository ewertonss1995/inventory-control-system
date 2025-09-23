package br.com.training.inventory_control_system.adapter.out.mappers;

import br.com.training.inventory_control_system.adapter.in.controllers.category.request.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.domain.entities.Category;
import br.com.training.inventory_control_system.mocks.CategoryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static br.com.training.inventory_control_system.mocks.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private CategoryMapper CategoriesMapper;

    private static CategoryRequest CATEGORY_REQUEST;
    private static Category CATEGORY;

    @BeforeEach
    void setUp() {
        CategoriesMapper = Mappers.getMapper(CategoryMapper.class);

        CATEGORY_REQUEST = CategoryMock.getCategoryRequestMock();
        CATEGORY = CategoryMock.getCategoryMock();
    }

    @Test
    void testToEntity() {
        Category category = CategoriesMapper.toEntity(CATEGORY_REQUEST);
        assertEquals(CATEGORY_REQUEST.getCategoryName(), category.getCategoryName());
    }

    @Test
    void testToEntityWithRequestNull() {
        Category category = CategoriesMapper.toEntity(null);
        assertNull(category);
    }

    @Test
    void testToGetCategoriesResponse() {
        GetCategoryResponse response = CategoriesMapper.toGetCategoryResponse(CATEGORY);

        assertEquals(ID_MOCK, response.getCategoryId());
        assertEquals(CATEGORY_NAME_MOCK, response.getCategoryName());
        assertEquals(DATE_NOW_MOCK, response.getRegistrationDate());
        assertNull(response.getUpdateDate());
    }

    @Test
    void testToGetCategoriesResponseWithEntityNull() {
        GetCategoryResponse response = CategoriesMapper.toGetCategoryResponse(null);
        assertNull(response);
    }

    @Test
    void testToGetCategoriesResponseList() {
        List<Category> CategoryList = Collections.singletonList(CATEGORY);

        List<GetCategoryResponse> responseList = CategoriesMapper.toGetCategoryResponseList(CategoryList);

        assertEquals(1, responseList.size());
        assertEquals(ID_MOCK, responseList.get(0).getCategoryId());
        assertEquals(CATEGORY_NAME_MOCK, responseList.get(0).getCategoryName());
        assertEquals(DATE_NOW_MOCK, responseList.get(0).getRegistrationDate());
        assertNull(responseList.get(0).getUpdateDate());

    }

    @Test
    void testToGetCategoriesResponseListWithCategoriesListNull() {
        List<GetCategoryResponse> responseList = CategoriesMapper.toGetCategoryResponseList(null);
        assertNull(responseList);
    }

    @Test
    void testUpdateEntityFromRequest() {
        Category existingCategories = new Category();

        CategoriesMapper.updateEntityFromRequest(CATEGORY_REQUEST, existingCategories);

        assertEquals(CATEGORY_NAME_MOCK, existingCategories.getCategoryName());
    }

    @Test
    void testUpdateEntityFromRequestWithRequestNull() {
        CategoriesMapper.updateEntityFromRequest(null, CATEGORY);

        assertEquals(ID_MOCK, CATEGORY.getCategoryId());
        assertEquals(CATEGORY_NAME_MOCK, CATEGORY.getCategoryName());
        assertEquals(DATE_NOW_MOCK, CATEGORY.getRegistrationDate());
        assertNull(CATEGORY.getUpdateDate());
    }
}
