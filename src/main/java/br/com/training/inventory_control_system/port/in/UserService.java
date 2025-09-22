package br.com.training.inventory_control_system.port.in;

import br.com.training.inventory_control_system.adapter.in.requests.UserRequest;
import br.com.training.inventory_control_system.domain.entities.User;

import java.util.List;

public interface UserService {
    void createUser(UserRequest request);
    void createAdminUser(UserRequest request);
    List<User> getUsers();
}
