package br.com.training.inventory_control_system.adapter.in.controllers.user.request;

import jakarta.validation.constraints.AssertTrue;

public record UpdateUserRequest(String userName, String password) {

    @AssertTrue(message = "At least one field must be filled: userName or password.")
    public boolean isAtLeastOneFieldNotEmpty() {
        return (userName != null && !userName.isEmpty()) ||
                (password != null && !password.isEmpty());
    }
}
