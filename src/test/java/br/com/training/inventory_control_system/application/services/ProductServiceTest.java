package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.ProductMapper;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.application.exception.ProductCustomException;
import br.com.training.inventory_control_system.application.exception.ProductNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Product;
import br.com.training.inventory_control_system.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;
    private GetProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest();
        product = new Product();
        productResponse = new GetProductResponse();
    }

    @Test
    void testSaveProduct() {
        when(mapper.toEntity(productRequest)).thenReturn(product);
        productService.saveProduct(productRequest);
        verify(repository, times(1)).save(product);
    }

    @Test
    void testSaveProductThrowsException() {
        when(mapper.toEntity(productRequest)).thenReturn(product);
        doThrow(new RuntimeException("Error")).when(repository).save(any(Product.class));

        ProductCustomException exception = assertThrows(ProductCustomException.class, () -> {
            productService.saveProduct(productRequest);
        });

        assertEquals("Unable to save product: Error", exception.getMessage());
        verify(repository, times(1)).save(product);
    }

    @Test
    void testGetProduct() {
        when(repository.findById(1)).thenReturn(Optional.of(product));
        when(mapper.toGetProductResponse(product)).thenReturn(productResponse);

        GetProductResponse response = productService.getProduct(1);

        assertEquals(productResponse, response);
        verify(repository, times(1)).findById(1);
        verify(mapper, times(1)).toGetProductResponse(product);
    }

    @Test
    void testGetProductThrowsNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProduct(1);
        });

        assertEquals("Unable to get product with ID: 1", exception.getMessage());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetProducts() {
        when(repository.findAll()).thenReturn(Collections.singletonList(product));
        when(mapper.toGetProductResponseList(anyList())).thenReturn(Collections.singletonList(productResponse));

        List<GetProductResponse> response = productService.getProducts();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(productResponse, response.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetProductsThrowsException() {
        when(repository.findAll()).thenThrow(new RuntimeException("Error"));

        ProductCustomException exception = assertThrows(ProductCustomException.class, () -> {
            productService.getProducts();
        });

        assertEquals("Unable to get products: Error", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        when(repository.findById(1)).thenReturn(Optional.of(product));
        doNothing().when(mapper).updateEntityFromRequest(productRequest, product);
        when(repository.save(product)).thenReturn(product);

        productService.updateProduct(1, productRequest);

        verify(repository, times(1)).findById(1);
        verify(mapper, times(1)).updateEntityFromRequest(productRequest, product);
        verify(repository, times(1)).save(product);
    }

    @Test
    void testUpdateProductThrowsNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        ProductCustomException exception = assertThrows(ProductCustomException.class, () -> {
            productService.updateProduct(1, productRequest);
        });

        assertEquals("Unable to update product: Unable to get product with ID 1 for update.", exception.getMessage());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testDeleteProduct() {
        when(repository.findById(1)).thenReturn(Optional.of(product));
        doNothing().when(repository).delete(product);

        productService.deleteProduct(1);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(product);
    }

    @Test
    void testDeleteProductThrowsNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1);
        });

        assertEquals("Unable to get product with ID 1 for update.", exception.getMessage());
        verify(repository, times(1)).findById(1);
    }
}
