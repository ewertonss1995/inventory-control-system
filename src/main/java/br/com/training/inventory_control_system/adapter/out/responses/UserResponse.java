package br.com.training.inventory_control_system.adapter.out.responses;

import br.com.training.inventory_control_system.domain.entities.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponse (
        UUID userId,
        String userName,
        Set<Role> roles
) {
}
