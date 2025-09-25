package br.com.training.inventory_control_system.adapter.in.controllers.user.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;

public record UpdateUserRequest(@Email String userEmail, String userName, String password) {

    @AssertTrue(message = "At least one field must be filled: uerEmail, userName or password.")
    public boolean isAtLeastOneFieldNotEmpty() {
        return (userEmail != null && !userEmail.isEmpty()) ||
                (userName != null && !userName.isEmpty()) ||
                (password != null && !password.isEmpty());
    }
}
