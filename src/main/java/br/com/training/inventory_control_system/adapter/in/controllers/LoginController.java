package br.com.training.inventory_control_system.adapter.in.controllers;

import br.com.training.inventory_control_system.adapter.in.requests.LoginRequest;
import br.com.training.inventory_control_system.adapter.out.responses.LoginResponse;
import br.com.training.inventory_control_system.port.in.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/auth", produces = APPLICATION_JSON_VALUE)
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity
                .ok(new LoginResponse(loginService.login(request), 300L));
    }
}
