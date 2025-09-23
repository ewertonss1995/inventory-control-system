package br.com.training.inventory_control_system.adapter.in.controllers.user.api;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.UserLoginResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.application.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static br.com.training.inventory_control_system.mocks.UserMock.getUserResponseMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserRequestMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = getUserRequestMock();
        userResponse = getUserResponseMock();
    }

    @Test
    void testUserLogin() {
        String expectedToken = "token";

        when(userServiceImpl.userLogin(userRequest)).thenReturn(expectedToken);

        ResponseEntity<UserLoginResponse> response = userController.userLogin(userRequest);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody().acessToken());
        assertEquals(300L, response.getBody().expiresIn());

        verify(userServiceImpl, times(1)).userLogin(userRequest);
    }

    @Test
    void testCreateUser() {
        userController.createUser(userRequest);
        verify(userServiceImpl, times(1)).createUser(userRequest);
    }

    @Test
    void testCreateAdminUser() {
        userController.createAdminUser(userRequest);
        verify(userServiceImpl, times(1)).createAdminUser(userRequest);
    }

    @Test
    void testGetUsers() {
        when(userServiceImpl.getUsers()).thenReturn(Collections.singletonList(userResponse));

        ResponseEntity<List<UserResponse>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(userResponse, response.getBody().get(0));
        verify(userServiceImpl, times(1)).getUsers();
    }
}
