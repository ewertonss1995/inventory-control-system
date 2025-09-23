package br.com.training.inventory_control_system.port.in;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;

import java.util.List;

public interface UserService {
    String userLogin(UserRequest request);
    void createUser(UserRequest request);
    void createAdminUser(UserRequest request);
    List<UserResponse> getUsers();
}
