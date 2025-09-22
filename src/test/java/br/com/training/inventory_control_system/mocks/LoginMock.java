package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.adapter.in.controllers.login.request.LoginRequest;

import static br.com.training.inventory_control_system.mocks.Constants.*;

public class LoginMock {

    public static LoginRequest getLoginRequestMock() {
        return new LoginRequest(USER_MOCK, PASSWORD_MOCK);
    }
}
