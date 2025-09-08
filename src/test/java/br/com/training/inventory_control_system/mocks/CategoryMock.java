package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.adapter.in.requests.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.domain.entities.Category;
import br.com.training.inventory_control_system.domain.entities.Product;

import java.util.List;

import static br.com.training.inventory_control_system.mocks.Constants.CATEGORY_NAME_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.CATEGORY_ID_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.DATE_NOW_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.ID_MOCK;

public class CategoryMock {

    public static CategoryRequest getCategoryRequestMock() {
        return new CategoryRequest(CATEGORY_NAME_MOCK);
    }

    public static Category getCategoryMock() {
        return new Category(
                CATEGORY_ID_MOCK,
                CATEGORY_NAME_MOCK,
                DATE_NOW_MOCK,
                null,
                List.of(new Product()));
    }

    public static GetCategoryResponse getCategoryResponseMock() {
        return new GetCategoryResponse(ID_MOCK, CATEGORY_NAME_MOCK, DATE_NOW_MOCK, null);
    }
}
