package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UpdateUserRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.UserMapper;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.application.exception.user.UserNotFoundException;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static br.com.training.inventory_control_system.mocks.Constants.USER_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.UUID_MOCK;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleAdmin;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleBasic;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleListMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserWithAllRolesMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserRequestMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserListMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserResponseListMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUpdateUserRequestMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserWithBasicRoleMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserResponseMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
    private UpdateUserRequest updateUserRequestMock;
    private User userMock;
    private User userBasicMock;
    private Role basicRoleMock;
    private Role adminRoleMock;

    @BeforeEach
    void setUp() {
        userRequest = getUserRequestMock();
        userMock = getUserWithAllRolesMock();
        userBasicMock = getUserWithBasicRoleMock();
        basicRoleMock = getRoleBasic();
        adminRoleMock = getRoleAdmin();
        userListMock = getUserListMock();
        userResponseListMock = getUserResponseListMock();
        updateUserRequestMock = getUpdateUserRequestMock() ;
    }

    @Test
    void testGetUserById() {
        UserResponse userResponseMock = getUserResponseMock();
        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.of(userMock));
        when(userMapper.toUserResponse(userMock)).thenReturn(userResponseMock);

        UserResponse response = userServiceImpl.getUserById(UUID_MOCK);

        assertEquals(userResponseMock, response);
        verify(userRepository, times(1)).findById(UUID_MOCK);
        verify(userMapper, times(1)).toUserResponse(userMock);
    }

    @Test
    void testGetUserByIdThrowsNotFoundException() {
        String expectedMessage = "User with ID ".concat(UUID_MOCK.toString()).concat(" was not found.");
        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceImpl.getUserById(UUID_MOCK);
        });

        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findById(UUID_MOCK);
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

    @Test
    void testUpdateUser() {
        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.of(userBasicMock));
        doNothing().when(userMapper).updateEntityFromRequest(updateUserRequestMock, userBasicMock);
        when(userRepository.save(userBasicMock)).thenReturn(userBasicMock);

        userServiceImpl.updateUser(UUID_MOCK, updateUserRequestMock);

        verify(userRepository, times(1)).findById(UUID_MOCK);
        verify(userMapper, times(1)).updateEntityFromRequest(updateUserRequestMock, userBasicMock);
        verify(userRepository, times(1)).save(userBasicMock);
    }

    @Test
    void testUpdateUserShouldThrowsIllegalArgumentException() {
        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.of(userMock));
        doNothing().when(userMapper).updateEntityFromRequest(updateUserRequestMock, userMock);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> userServiceImpl.updateUser(UUID_MOCK, updateUserRequestMock));

        assertEquals("You do not have permission to change an administrator user's information.", exception.getMessage());
        verify(userRepository, times(1)).findById(UUID_MOCK);
        verify(userRepository, never()).save(userMock);
    }

    @Test
    void testUpdateUserThrowsUserNotFoundException() {
        String expectedMessage = "User with ID ".concat(UUID_MOCK.toString()).concat(" was not found.");

        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceImpl.updateUser(UUID_MOCK, updateUserRequestMock);
        });

        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findById(UUID_MOCK);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.of(userBasicMock));
        doNothing().when(userRepository).delete(userBasicMock);

        userServiceImpl.deleteUser(UUID_MOCK);

        verify(userRepository, times(1)).findById(UUID_MOCK);
        verify(userRepository, times(1)).delete(userBasicMock);
    }

     @Test
    void testDeleteUserShouldThrowsNoSuchElementException() {
        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.of(userMock));

         IllegalArgumentException exception = assertThrows(
                 IllegalArgumentException.class, () -> userServiceImpl.deleteUser(UUID_MOCK));

        assertEquals("You do not have permission to delete an administrator user.", exception.getMessage());
        verify(userRepository, times(1)).findById(UUID_MOCK);
        verify(userRepository, never()).delete(userMock);
    }

    @Test
    void testDeleteUserThrowsNotFoundException() {
        String expectedMessage = "User with ID ".concat(UUID_MOCK.toString()).concat(" was not found.");
        when(userRepository.findById(UUID_MOCK)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceImpl.deleteUser(UUID_MOCK);
        });

        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findById(UUID_MOCK);
    }
}
