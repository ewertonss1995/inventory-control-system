package br.com.training.inventory_control_system.adapter.exception.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiErrorResponse {
    private String message;
    private int status;
}
