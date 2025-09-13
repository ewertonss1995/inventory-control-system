package br.com.training.inventory_control_system.adapter.exception.handler;

import br.com.training.inventory_control_system.adapter.exception.handle.GlobalExceptionHandler;
import br.com.training.inventory_control_system.adapter.exception.response.ApiErrorResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.product.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpMessageNotReadableException httpMessageNotReadableException;

    @Mock
    private DataAccessException dataAccessException;

    @Mock
    private GeneralCustomException generalCustomException;

    @Mock
    private RuntimeException runtimeException;

    @Mock
    private ProductNotFoundException movimentoNaoEncontradoException;

    @Test
    void testHandleValidationExceptions() {
        FieldError fieldError = new FieldError("objectName", "fieldName", "Invalid value");
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Invalid value", response.getBody().get("fieldName"));
    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(httpMessageNotReadableException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The body of the request is empty or invalid. Please provide valid data.", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleDataAccessException() {
            DataAccessException dataAccessException = new DataIntegrityViolationException("Database connection error");
            ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleDataAccessException(dataAccessException);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Error communicating with the database: Database connection error", response.getBody().getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("Integrity violation");
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolationException(dataIntegrityViolationException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An integrity error occurred while performing the operation on the database: Integrity violation", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleEmptyResultDataAccessException() {
        EmptyResultDataAccessException emptyResultDataAccessException =
                new EmptyResultDataAccessException("Product not found", 1);

        ResponseEntity<ApiErrorResponse> response =
                globalExceptionHandler.handleEmptyResultDataAccessException(emptyResultDataAccessException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error during execution: Product not found",
                response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleGeneralCustomException() {
        when(generalCustomException.getMessage()).thenReturn("Error saving movement");
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleGeneralCustomException(generalCustomException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error processing request: Error saving movement", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleRuntimeException() {
        when(runtimeException.getMessage()).thenReturn("Unexpected error");
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleRuntimeException(runtimeException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error during the execution of the request: Unexpected error", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleRuntimeExceptionComMovimentoNaoEncontrado() {
        when(movimentoNaoEncontradoException.getMessage()).thenReturn("Movement not found");
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleRuntimeException(movimentoNaoEncontradoException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error during the execution of the request: Movement not found", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }
}
