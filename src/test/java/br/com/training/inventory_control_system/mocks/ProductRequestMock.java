package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.domain.entities.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductRequestMock {

    public static final String NAME_MOCK = "Product test";
    public static final String DESCRIPTION_MOCK = "Product Description";
    public static final BigDecimal UNIT_PRICE_MOCK = BigDecimal.valueOf(10.00);
    public static final int QUANTITY_MOCK = 10;
    public static final String CATEGORY_MOCK = "Product category";
    public static final LocalDateTime DATE_NOW_MOCK = LocalDateTime.now();
    public static final int ID_MOCK = 1;
    public static final BigDecimal TOTAL_PRICE_MOCK = BigDecimal.valueOf(100.00);

    public static ProductRequest getProductRequestMock() {
        return new ProductRequest(
                NAME_MOCK,
                DESCRIPTION_MOCK,
                UNIT_PRICE_MOCK,
                QUANTITY_MOCK,
                CATEGORY_MOCK,
                DATE_NOW_MOCK,
                null);
    }

    public static Product getProductMock() {
        return new Product(
                ID_MOCK,
                NAME_MOCK,
                DESCRIPTION_MOCK,
                UNIT_PRICE_MOCK,
                QUANTITY_MOCK,
                TOTAL_PRICE_MOCK,
                CATEGORY_MOCK,
                DATE_NOW_MOCK,
                null,
                null);
    }

    public static GetProductResponse getGetProductResponseMock() {
        return new GetProductResponse(
                ID_MOCK,
                NAME_MOCK,
                DESCRIPTION_MOCK,
                UNIT_PRICE_MOCK,
                QUANTITY_MOCK,
                TOTAL_PRICE_MOCK,
                CATEGORY_MOCK,
                DATE_NOW_MOCK,
                null);
    }
}
