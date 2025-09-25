package br.com.training.inventory_control_system.adapter.in.controllers.user.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest (@Email String userEmail, String userName, @NotBlank String password) {

    @AssertTrue(message = "At least one field must be filled: userEmail or userName.")
    public boolean isAtLeastOneFieldNotEmpty() {
        return (userEmail != null && !userEmail.isEmpty()) ||
                (userName != null && !userName.isEmpty());
    }
}
