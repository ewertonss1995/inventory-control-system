package br.com.training.inventory_control_system.adapter.out.responses;

import lombok.Builder;

@Builder
public record ApiResponse(
    String message,
    int status
) {}
