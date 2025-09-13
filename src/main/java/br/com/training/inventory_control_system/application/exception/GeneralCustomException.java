package br.com.training.inventory_control_system.application.exception;

public class GeneralCustomException extends RuntimeException {
    public GeneralCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
