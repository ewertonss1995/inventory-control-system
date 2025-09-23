package br.com.training.inventory_control_system.adapter.exception.handle;

import br.com.training.inventory_control_system.adapter.exception.response.ApiErrorResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LOGGER.error("Required fields were not found in the request body: {}", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOGGER.error("The body of the request is empty or invalid: {}", ex.getMessage(), ex);
        return buildResponseMessage(
                "The body of the request is empty or invalid. Please provide valid data.",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccessException(DataAccessException ex) {
        LOGGER.error("There was an unexpected error while communicating with the database: {}", ex.getMessage(), ex);
        return buildResponseMessage("Error communicating with the database: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        LOGGER.error("Database access error: {}", ex.getMessage(), ex);
        return buildResponseMessage(
                "An integrity error occurred while performing the operation on the database: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        LOGGER.error("Information not found: {}", ex.getMessage(), ex);
        return buildResponseMessage(
                "Error during execution: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GeneralCustomException.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralCustomException(GeneralCustomException ex) {
        LOGGER.error("There was an error processing the request: {}", ex.getMessage(), ex);
        return buildResponseMessage(
                "Error processing request: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        LOGGER.error("There was an error during execution: {}", ex.getMessage(), ex);

        return buildResponseMessage(
                "Invalid user: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        LOGGER.error("There was an error during execution: {}", ex.getMessage(), ex);

        return buildResponseMessage(
                "User is not authorized to make this request: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex) {
        LOGGER.error("There was an unexpected error during execution: {}", ex.getMessage(), ex);

        return buildResponseMessage(
                "Error during the execution of the request: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponse> buildResponseMessage(String mensagem, HttpStatus httpStatus) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(mensagem)
                .status(httpStatus.value())
                .build(),
                httpStatus);
    }
}
