package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.product.request.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.ProductMapper;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.product.ProductNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Product;
import br.com.training.inventory_control_system.domain.repositories.ProductRepository;
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

import static br.com.training.inventory_control_system.mocks.ProductMock.getProductMock;
import static br.com.training.inventory_control_system.mocks.ProductMock.getProductRequestMock;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private ProductRequest productRequest;
    private Product product;
    private GetProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = getProductRequestMock();
        product = getProductMock();
        productResponse = new GetProductResponse();
    }

    @Test
    void testSaveProduct() {
        when(mapper.toEntity(productRequest)).thenReturn(product);
        productServiceImpl.saveProduct(productRequest);
        verify(repository, times(1)).save(product);
    }

    @Test
    void testSaveProductThrowsException() {
        when(mapper.toEntity(productRequest)).thenReturn(product);
        doThrow(new RuntimeException("Error")).when(repository).save(any(Product.class));

        GeneralCustomException exception = assertThrows(GeneralCustomException.class, () -> {
            productServiceImpl.saveProduct(productRequest);
        });

        assertEquals("Unable to save product: Error", exception.getMessage());
        verify(repository, times(1)).save(product);
    }

    @Test
    void testGetProduct() {
        when(repository.findProductWithCategory(1)).thenReturn(Optional.of(product));
        when(mapper.toGetProductResponse(product)).thenReturn(productResponse);

        GetProductResponse response = productServiceImpl.getProduct(1);

        assertEquals(productResponse, response);
        verify(repository, times(1)).findProductWithCategory(1);
        verify(mapper, times(1)).toGetProductResponse(product);
    }

    @Test
    void testGetProductThrowsNotFoundException() {
        when(repository.findProductWithCategory(1)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productServiceImpl.getProduct(1);
        });

        assertEquals("Product with ID 1 was not found.", exception.getMessage());
        verify(repository, times(1)).findProductWithCategory(1);
    }

    @Test
    void testGetProducts() {
        when(repository.findAll()).thenReturn(Collections.singletonList(product));
        when(mapper.toGetProductResponseList(anyList())).thenReturn(Collections.singletonList(productResponse));

        List<GetProductResponse> response = productServiceImpl.getProducts();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(productResponse, response.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetProductsThrowsException() {
        when(repository.findAll()).thenThrow(new RuntimeException("Error"));

        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class, () -> {
            productServiceImpl.getProducts();
        });

        assertEquals("Unable to get products: Error", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        when(repository.findProductWithCategory(1)).thenReturn(Optional.of(product));
        doNothing().when(mapper).updateEntityFromRequest(productRequest, product);
        when(repository.save(product)).thenReturn(product);

        productServiceImpl.updateProduct(1, productRequest);

        verify(repository, times(1)).findProductWithCategory(1);
        verify(mapper, times(1)).updateEntityFromRequest(productRequest, product);
        verify(repository, times(1)).save(product);
    }

    @Test
    void testUpdateProductThrowsNotFoundException() {
        when(repository.findProductWithCategory(1)).thenReturn(Optional.empty());

        GeneralCustomException exception = assertThrows(GeneralCustomException.class, () -> {
            productServiceImpl.updateProduct(1, productRequest);
        });

        assertEquals("Error updating product: Product with ID 1 was not found.", exception.getMessage());
        verify(repository, times(1)).findProductWithCategory(1);
    }

    @Test
    void testDeleteProduct() {
        when(repository.findById(1)).thenReturn(Optional.of(product));
        doNothing().when(repository).delete(product);

        productServiceImpl.deleteProduct(1);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(product);
    }

    @Test
    void testDeleteProductThrowsNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productServiceImpl.deleteProduct(1);
        });

        assertEquals("Product with ID 1 was not found.", exception.getMessage());
        verify(repository, times(1)).findById(1);
    }
}
