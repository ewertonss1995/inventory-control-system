package br.com.training.inventory_control_system.port.in;

import br.com.training.inventory_control_system.adapter.in.requests.LoginRequest;

public interface LoginService {
    String login(LoginRequest request);
}
