package br.com.training.inventory_control_system.port.in;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.RefreshTokenRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UpdateUserRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.RefreshTokenResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserLoginResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserLoginResponse userLogin(UserRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    void logout(UUID userId);
    void createUser(UserRequest request);
    void createAdminUser(UserRequest request);
    List<UserResponse> getUsers();
    UserResponse getUserById(UUID userId);
    void updateUser(UUID userId, UpdateUserRequest request);
    void deleteUser(UUID userId);
}
