package br.com.training.inventory_control_system.adapter.out.mappers;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.domain.entities.Product;
import br.com.training.inventory_control_system.mocks.ProductRequestMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static br.com.training.inventory_control_system.mocks.ProductRequestMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProductMapperTest {

    private ProductMapper productMapper;

    private static ProductRequest PRODUCT_REQUEST;
    private static Product PRODUCT;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);

        PRODUCT_REQUEST = ProductRequestMock.getProductRequestMock();
        PRODUCT = ProductRequestMock.getProductMock();
    }

    @Test
    void testToEntity() {
        Product product = productMapper.toEntity(PRODUCT_REQUEST);

        assertEquals(NAME_MOCK, product.getProductName());
        assertEquals(DESCRIPTION_MOCK, product.getProductDescription());
        assertEquals(UNIT_PRICE_MOCK, product.getUnitPrice());
        assertEquals(QUANTITY_MOCK, product.getQuantity());
        assertEquals(CATEGORY_MOCK, product.getCategory());
        assertEquals(DATE_NOW_MOCK, product.getRegistrationDate());
        assertNull(product.getRemoveDate());
    }

    @Test
    void testToEntityWithRequestNull() {
        Product product = productMapper.toEntity(null);
        assertNull(product);
    }

    @Test
    void testToGetProductResponse() {
        GetProductResponse response = productMapper.toGetProductResponse(PRODUCT);

        assertEquals(ID_MOCK, response.getProductId());
        assertEquals(NAME_MOCK, response.getProductName());
        assertEquals(DESCRIPTION_MOCK, response.getProductDescription());
        assertEquals(UNIT_PRICE_MOCK, response.getUnitPrice());
        assertEquals(QUANTITY_MOCK, response.getQuantity());
        assertEquals(TOTAL_PRICE_MOCK, response.getTotalPrice());
        assertEquals(CATEGORY_MOCK, response.getCategory());
        assertEquals(DATE_NOW_MOCK, response.getRegistrationDate());
        assertNull(response.getRemoveDate());
    }

    @Test
    void testToGetProductResponseWithEntityNull() {
        GetProductResponse response = productMapper.toGetProductResponse(null);
        assertNull(response);
    }

    @Test
    void testToGetProductResponseList() {
        List<Product> products = Collections.singletonList(PRODUCT);

        List<GetProductResponse> responseList = productMapper.toGetProductResponseList(products);

        assertEquals(1, responseList.size());
        assertEquals(ID_MOCK, responseList.get(0).getProductId());
        assertEquals(NAME_MOCK, responseList.get(0).getProductName());
        assertEquals(DESCRIPTION_MOCK, responseList.get(0).getProductDescription());
        assertEquals(UNIT_PRICE_MOCK, responseList.get(0).getUnitPrice());
        assertEquals(QUANTITY_MOCK, responseList.get(0).getQuantity());
        assertEquals(TOTAL_PRICE_MOCK, responseList.get(0).getTotalPrice());
        assertEquals(CATEGORY_MOCK, responseList.get(0).getCategory());
        assertEquals(DATE_NOW_MOCK, responseList.get(0).getRegistrationDate());
        assertNull(responseList.get(0).getRemoveDate());

    }

    @Test
    void testToGetProductResponseListWithProductListNull() {
        List<GetProductResponse> responseList = productMapper.toGetProductResponseList(null);
        assertNull(responseList);
    }

    @Test
    void testUpdateEntityFromRequest() {
        Product existingProduct = new Product();

        productMapper.updateEntityFromRequest(PRODUCT_REQUEST, existingProduct);

        assertEquals(NAME_MOCK, existingProduct.getProductName());
        assertEquals(DESCRIPTION_MOCK, existingProduct.getProductDescription());
        assertEquals(UNIT_PRICE_MOCK, existingProduct.getUnitPrice());
        assertEquals(QUANTITY_MOCK, existingProduct.getQuantity());
        assertEquals(CATEGORY_MOCK, existingProduct.getCategory());
        assertNotEquals(DATE_NOW_MOCK, existingProduct.getRegistrationDate());
        assertNull(existingProduct.getRemoveDate());
    }

    @Test
    void testUpdateEntityFromRequestWithRequestNull() {
        productMapper.updateEntityFromRequest(null, PRODUCT);

        assertEquals(NAME_MOCK, PRODUCT.getProductName());
        assertEquals(DESCRIPTION_MOCK, PRODUCT.getProductDescription());
        assertEquals(UNIT_PRICE_MOCK, PRODUCT.getUnitPrice());
        assertEquals(QUANTITY_MOCK, PRODUCT.getQuantity());
        assertEquals(CATEGORY_MOCK, PRODUCT.getCategory());
        assertEquals(DATE_NOW_MOCK, PRODUCT.getRegistrationDate());
        assertNull(PRODUCT.getRemoveDate());
        assertNull(PRODUCT.getUpdateDate());

    }
}
