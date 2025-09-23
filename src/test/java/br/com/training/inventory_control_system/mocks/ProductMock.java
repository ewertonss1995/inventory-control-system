package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.adapter.in.controllers.product.request.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.domain.entities.Category;
import br.com.training.inventory_control_system.domain.entities.Product;

import java.util.List;

import static br.com.training.inventory_control_system.mocks.Constants.*;

public class ProductMock {

    public static ProductRequest getProductRequestMock() {
        return new ProductRequest(
                NAME_MOCK,
                DESCRIPTION_MOCK,
                UNIT_PRICE_MOCK,
                QUANTITY_MOCK,
                ID_MOCK);
    }

    public static Product getProductMock() {
        return new Product(
                ID_MOCK,
                NAME_MOCK,
                DESCRIPTION_MOCK,
                UNIT_PRICE_MOCK,
                QUANTITY_MOCK,
                TOTAL_PRICE_MOCK,
                DATE_NOW_MOCK,
                null,
                getCategory());
    }

    public static GetProductResponse getGetProductResponseMock() {
        return new GetProductResponse(
                ID_MOCK,
                NAME_MOCK,
                DESCRIPTION_MOCK,
                UNIT_PRICE_MOCK,
                QUANTITY_MOCK,
                TOTAL_PRICE_MOCK,
                getGetCategoryResponse(),
                DATE_NOW_MOCK,
                null);
    }

    private static GetCategoryResponse getGetCategoryResponse() {
        return new GetCategoryResponse(ID_MOCK, CATEGORY_NAME_MOCK, DATE_NOW_MOCK, null);
    }

    private static Category getCategory() {
        return new Category(
                ID_MOCK,
                CATEGORY_NAME_MOCK,
                DATE_NOW_MOCK,
                null,
                List.of(new Product()));
    }
}
