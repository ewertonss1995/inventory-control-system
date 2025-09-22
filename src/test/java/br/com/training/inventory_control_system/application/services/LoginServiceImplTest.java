package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.requests.LoginRequest;
import br.com.training.inventory_control_system.domain.entities.User;
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

import java.util.Optional;

import static br.com.training.inventory_control_system.mocks.Constants.UUID_MOCK;
import static br.com.training.inventory_control_system.mocks.LoginMock.getLoginRequestMock;
import static br.com.training.inventory_control_system.mocks.RoleMock.getRoleListMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Jwt jwtMock;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock private User user;

    @InjectMocks
    private LoginServiceImpl loginServiceImpl;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = getLoginRequestMock();
    }

    @Test
    void testLoginSuccessful() {
        when(userRepository.findByUserName(loginRequest.userName())).thenReturn(Optional.of(user));
        when(user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)).thenReturn(true);

        when(user.getRoles()).thenReturn(getRoleListMock());
        when(user.getUserId()).thenReturn(UUID_MOCK);

        when(jwtMock.getTokenValue()).thenReturn("jwt.token.value");

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtMock);

        String token = loginServiceImpl.login(loginRequest);

        assertEquals("jwt.token.value", token);
        verify(userRepository, times(1)).findByUserName(loginRequest.userName());
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void testLoginInvalidUser() {
        when(userRepository.findByUserName(loginRequest.userName())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class, () -> loginServiceImpl.login(loginRequest));

        assertEquals("User or password is invalid!", exception.getMessage());
        verify(userRepository, times(1)).findByUserName(loginRequest.userName());
    }

    @Test
    void testLoginIncorrectPassword() {
        when(userRepository.findByUserName(loginRequest.userName())).thenReturn(Optional.of(user));
        when(user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)).thenReturn(false);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class, () -> loginServiceImpl.login(loginRequest));

        assertEquals("User or password is invalid!", exception.getMessage());
        verify(userRepository, times(1)).findByUserName(loginRequest.userName());
    }
}
