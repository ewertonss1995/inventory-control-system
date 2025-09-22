package br.com.training.inventory_control_system.adapter.in.controllers;

import br.com.training.inventory_control_system.adapter.in.requests.UserRequest;
import br.com.training.inventory_control_system.application.services.UserServiceImpl;
import br.com.training.inventory_control_system.domain.entities.User;
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

import static br.com.training.inventory_control_system.mocks.UserMock.getUserMock;
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
    private User user;

    @BeforeEach
    void setUp() {
        userRequest = getUserRequestMock();
        user = getUserMock();
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
        when(userServiceImpl.getUsers()).thenReturn(Collections.singletonList(user));

        ResponseEntity<List<User>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(user, response.getBody().get(0));
        verify(userServiceImpl, times(1)).getUsers();
    }
}
