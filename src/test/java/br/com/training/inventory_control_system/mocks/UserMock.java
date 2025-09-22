package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.adapter.in.requests.UserRequest;
import br.com.training.inventory_control_system.domain.entities.Role;
import br.com.training.inventory_control_system.domain.entities.User;

import java.util.Set;

import static br.com.training.inventory_control_system.mocks.Constants.PASSWORD_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.USER_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.ROLE_ADMIN_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.ROLE_BASIC_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.UUID_MOCK;

public class UserMock {

    public static UserRequest getUserRequestMock() {
        return new UserRequest(USER_MOCK, PASSWORD_MOCK);
    }

    public static User getUserMock() {
        Role roleAdmin =  new Role(1, ROLE_ADMIN_MOCK);
        Role roleBasic = new Role(2, ROLE_BASIC_MOCK);

        Set<Role> roles = Set.of(roleAdmin, roleBasic);

        return new User(UUID_MOCK, USER_MOCK, PASSWORD_MOCK, roles);
    }
}
