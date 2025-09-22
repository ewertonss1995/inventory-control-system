package br.com.training.inventory_control_system.adapter.in.controllers;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.port.in.ProductService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService useCase;

    @InjectMocks
    private ProductController productController;

    private ProductRequest productRequest;
    private GetProductResponse getProductResponse;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest();
        productRequest.setProductName("Sample Product");

        getProductResponse = new GetProductResponse();
    }

    @Test
    void testSaveProduct() {
        doNothing().when(useCase).saveProduct(any(ProductRequest.class));

        ResponseEntity<ApiResponse> response = productController.saveProduct(productRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The product named 'Sample Product' has been successfully created.", response.getBody().message());
        verify(useCase, times(1)).saveProduct(productRequest);
    }

    @Test
    void testGetProduct() {
        when(useCase.getProduct(1)).thenReturn(getProductResponse);

        ResponseEntity<GetProductResponse> response = productController.getProduct(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getProductResponse, response.getBody());
        verify(useCase, times(1)).getProduct(1);
    }

    @Test
    void testGetProducts() {
        List<GetProductResponse> productList = Collections.singletonList(getProductResponse);
        when(useCase.getProducts()).thenReturn(productList);

        ResponseEntity<List<GetProductResponse>> response = productController.getProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productList, response.getBody());
        verify(useCase, times(1)).getProducts();
    }

    @Test
    void testUpdateProduct() {
        doNothing().when(useCase).updateProduct(any(Integer.class), any(ProductRequest.class));

        ResponseEntity<ApiResponse> response = productController.updateProduct(1, productRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product with ID 1 was updated successfully.", response.getBody().message());
        verify(useCase, times(1)).updateProduct(1, productRequest);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(useCase).deleteProduct(1);

        assertDoesNotThrow(() -> {
            productController.deletarRegistro(1);
        });

        verify(useCase, times(1)).deleteProduct(1);
    }
}
