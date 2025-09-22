package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.domain.entities.Role;
import br.com.training.inventory_control_system.domain.entities.User;
import br.com.training.inventory_control_system.domain.entities.enums.RolesEnum;
import br.com.training.inventory_control_system.domain.repositories.RoleRepository;
import br.com.training.inventory_control_system.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static br.com.training.inventory_control_system.mocks.Constants.USER_MOCK;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleBasic;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleAdmin;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserRequestMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserRequest userRequest;
    private User userMock;
    private Role basicRoleMock;
    private Role adminRoleMock;

    @BeforeEach
    void setUp() {
        userRequest = getUserRequestMock();
        userMock = getUserMock();
        basicRoleMock = getRoleBasic();
        adminRoleMock = getRoleAdmin();
    }

    @Test
    void testCreateUserWhenUserExistsThenThrowException() {
        when(userRepository.findByUserName(USER_MOCK)).thenReturn(Optional.of(userMock));
        assertThrows(ResponseStatusException.class, () -> userServiceImpl.createUser(userRequest));
    }

    @Test
    void testCreateUserWhenUserDoesNotExistThenCreateUser() {
        when(userRepository.findByUserName(userRequest.userName())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RolesEnum.BASIC.name())).thenReturn(basicRoleMock);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userServiceImpl.createUser(userRequest);

        verify(userRepository).save(any());
    }

    @Test
    void testCreateAdminUserWhenAdminExistsThenThrowException() {
        when(userRepository.findByUserName("admin")).thenReturn(Optional.of(userMock));
        assertThrows(ResponseStatusException.class, () -> userServiceImpl.createAdminUser(userRequest));
    }

    @Test
    void testCreateAdminUserWhenAdminDoesNotExistThenCreateAdminUser() {
        when(roleRepository.findByName(RolesEnum.ADMIN.name())).thenReturn(adminRoleMock);
        when(roleRepository.findByName(RolesEnum.BASIC.name())).thenReturn(basicRoleMock);
        when(userRepository.findByUserName("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userServiceImpl.createAdminUser(userRequest);

        verify(userRepository).save(any());
    }

    @Test
    void testGetUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userServiceImpl.getUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }
}
