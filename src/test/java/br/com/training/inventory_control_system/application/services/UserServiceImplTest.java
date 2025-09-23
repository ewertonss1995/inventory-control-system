package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.UserMapper;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static br.com.training.inventory_control_system.mocks.Constants.USER_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.UUID_MOCK;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleAdmin;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleBasic;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleListMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserRequestMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserListMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserResponseListMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Jwt jwtMock;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock private User user;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private List<User> userListMock;
    private List<UserResponse> userResponseListMock;
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
        userListMock = getUserListMock();
        userResponseListMock = getUserResponseListMock();
    }

    @Test
    void testUserLoginSuccessful() {
        when(userRepository.findByUserName(userRequest.userName())).thenReturn(Optional.of(user));
        when(user.isLoginCorrect(userRequest, bCryptPasswordEncoder)).thenReturn(true);

        when(user.getRoles()).thenReturn(getRoleListMock());
        when(user.getUserId()).thenReturn(UUID_MOCK);

        when(jwtMock.getTokenValue()).thenReturn("jwt.token.value");

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtMock);

        String token = userServiceImpl.userLogin(userRequest);

        assertEquals("jwt.token.value", token);
        verify(userRepository, times(1)).findByUserName(userRequest.userName());
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void testUserLoginInvalidUser() {
        when(userRepository.findByUserName(userRequest.userName())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class, () -> userServiceImpl.userLogin(userRequest));

        assertEquals("User or password is invalid!", exception.getMessage());
        verify(userRepository, times(1)).findByUserName(userRequest.userName());
    }

    @Test
    void testUserLoginIncorrectPassword() {
        when(userRepository.findByUserName(userRequest.userName())).thenReturn(Optional.of(user));
        when(user.isLoginCorrect(userRequest, bCryptPasswordEncoder)).thenReturn(false);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class, () -> userServiceImpl.userLogin(userRequest));

        assertEquals("User or password is invalid!", exception.getMessage());
        verify(userRepository, times(1)).findByUserName(userRequest.userName());
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
        when(userMapper.toEntity(userRequest)).thenReturn(userMock);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");

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

        when(userMapper.toEntity(userRequest)).thenReturn(userMock);

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userServiceImpl.createAdminUser(userRequest);

        verify(userRepository).save(any());
    }

    @Test
    void testGetUsers() {
        when(userRepository.findAll()).thenReturn(userListMock);
        when(userMapper.toUserResponseList(userListMock)).thenReturn(userResponseListMock);

        List<UserResponse> result = userServiceImpl.getUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }
}
