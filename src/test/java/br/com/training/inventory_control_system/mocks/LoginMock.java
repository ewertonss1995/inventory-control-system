package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.adapter.in.requests.LoginRequest;
import br.com.training.inventory_control_system.adapter.in.requests.UserRequest;
import br.com.training.inventory_control_system.domain.entities.Role;
import br.com.training.inventory_control_system.domain.entities.User;

import java.util.Set;

import static br.com.training.inventory_control_system.mocks.Constants.*;

public class LoginMock {

    public static LoginRequest getLoginRequestMock() {
        return new LoginRequest(USER_MOCK, PASSWORD_MOCK);
    }
}
