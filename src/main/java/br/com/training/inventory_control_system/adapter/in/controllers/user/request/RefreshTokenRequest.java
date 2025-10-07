package br.com.training.inventory_control_system.adapter.in.controllers.user.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {
}