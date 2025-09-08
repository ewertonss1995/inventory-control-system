package br.com.training.inventory_control_system.application.exception.category;

public class CategoryCustomException extends RuntimeException {
    public CategoryCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
