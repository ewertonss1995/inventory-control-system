package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UpdateUserRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.domain.entities.Role;
import br.com.training.inventory_control_system.domain.entities.User;

import java.util.List;
import java.util.Set;

import static br.com.training.inventory_control_system.mocks.Constants.PASSWORD_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.USER_NAME_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.USER_EMAIL_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.ROLE_ADMIN_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.ROLE_BASIC_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.UUID_MOCK;

public class UserMock {

    public static final String FIRST_USER = "First User";
    public static final String SECOND_USER = "Second User";

    public static UserRequest getUserRequestMock() {
        return new UserRequest(USER_EMAIL_MOCK, USER_NAME_MOCK, PASSWORD_MOCK);
    }

    public static UpdateUserRequest getUpdateUserRequestMock() {
        return new UpdateUserRequest(USER_EMAIL_MOCK, USER_NAME_MOCK, PASSWORD_MOCK);
    }


    public static User getUserWithAllRolesMock() {
        Role roleAdmin = new Role(1, ROLE_ADMIN_MOCK);
        Role roleBasic = new Role(2, ROLE_BASIC_MOCK);

        Set<Role> roles = Set.of(roleAdmin, roleBasic);

        return new User(UUID_MOCK, USER_NAME_MOCK, USER_EMAIL_MOCK, PASSWORD_MOCK, roles);
    }

    public static User getUserWithBasicRoleMock() {
        Role roleBasic = new Role(2, ROLE_BASIC_MOCK);

        Set<Role> roles = Set.of(roleBasic);

        return new User(UUID_MOCK, USER_NAME_MOCK, USER_EMAIL_MOCK, PASSWORD_MOCK, roles);
    }

    public static List<User> getUserListMock() {
        Role roleAdmin = new Role(1, ROLE_ADMIN_MOCK);
        Role roleBasic = new Role(2, ROLE_BASIC_MOCK);

        Set<Role> roles = Set.of(roleAdmin, roleBasic);

        User firstUser = new User(UUID_MOCK, FIRST_USER, USER_EMAIL_MOCK, PASSWORD_MOCK, roles);
        User secondUser = new User(UUID_MOCK, SECOND_USER, USER_EMAIL_MOCK, PASSWORD_MOCK, roles);

        return List.of(firstUser, secondUser);
    }

    public static UserResponse getUserResponseMock() {
        Role roleAdmin = new Role(1, ROLE_ADMIN_MOCK);
        Role roleBasic = new Role(2, ROLE_BASIC_MOCK);

        Set<Role> roles = Set.of(roleAdmin, roleBasic);

        return new UserResponse(UUID_MOCK, USER_NAME_MOCK, USER_EMAIL_MOCK, roles);
    }

    public static List<UserResponse> getUserResponseListMock() {
        Role roleAdmin = new Role(1, ROLE_ADMIN_MOCK);
        Role roleBasic = new Role(2, ROLE_BASIC_MOCK);

        Set<Role> roles = Set.of(roleAdmin, roleBasic);

        UserResponse firstUser = new UserResponse(UUID_MOCK, FIRST_USER, USER_EMAIL_MOCK, roles);
        UserResponse secondUser = new UserResponse(UUID_MOCK, SECOND_USER, USER_EMAIL_MOCK,roles);

        return List.of(firstUser, secondUser);
    }
}
