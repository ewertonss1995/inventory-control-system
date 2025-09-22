package br.com.training.inventory_control_system.adapter.in.controllers.login.api;

import br.com.training.inventory_control_system.adapter.in.controllers.login.request.LoginRequest;
import br.com.training.inventory_control_system.adapter.out.responses.LoginResponse;
import br.com.training.inventory_control_system.application.services.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginServiceImpl loginServiceImpl;

    @InjectMocks
    private LoginController loginController;

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("username", "password");
        String expectedToken = "token";

        when(loginServiceImpl.login(request)).thenReturn(expectedToken);

        ResponseEntity<LoginResponse> response = loginController.login(request);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody().acessToken());
        assertEquals(300L, response.getBody().expiresIn());

        verify(loginServiceImpl, times(1)).login(request);
    }
}
