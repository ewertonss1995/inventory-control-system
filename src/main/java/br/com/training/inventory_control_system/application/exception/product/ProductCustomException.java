package br.com.training.inventory_control_system.application.exception.product;

public class ProductCustomException extends RuntimeException {
    public ProductCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
