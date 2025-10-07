package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.application.exception.user.RefreshTokenExpiredException;
import br.com.training.inventory_control_system.application.exception.user.RefreshTokenNotFoundException;
import br.com.training.inventory_control_system.domain.entities.RefreshToken;
import br.com.training.inventory_control_system.domain.entities.User;
import br.com.training.inventory_control_system.domain.repositories.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User testUser;
    private RefreshToken testRefreshToken;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(UUID.randomUUID());
        testUser.setUserName("testuser");

        testRefreshToken = new RefreshToken();
        testRefreshToken.setTokenId(UUID.randomUUID());
        testRefreshToken.setToken(UUID.randomUUID().toString());
        testRefreshToken.setUser(testUser);
        testRefreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
    }

    @Test
    void testCreateRefreshToken() {
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        RefreshToken result = refreshTokenService.createRefreshToken(testUser);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        verify(refreshTokenRepository, times(1)).deleteByUser(testUser);
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testVerifyRefreshTokenValid() {
        when(refreshTokenRepository.findByToken(testRefreshToken.getToken()))
                .thenReturn(Optional.of(testRefreshToken));

        RefreshToken result = refreshTokenService.verifyRefreshToken(testRefreshToken.getToken());

        assertNotNull(result);
        assertEquals(testRefreshToken, result);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void testVerifyRefreshTokenNotFound() {
        String invalidToken = "invalid-token";
        when(refreshTokenRepository.findByToken(invalidToken))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, 
                () -> refreshTokenService.verifyRefreshToken(invalidToken));
    }

    @Test
    void testVerifyRefreshTokenExpired() {
        RefreshToken expiredToken = new RefreshToken();
        expiredToken.setToken(UUID.randomUUID().toString());
        expiredToken.setExpiryDate(Instant.now().minusSeconds(3600)); // Expired 1 hour ago

        when(refreshTokenRepository.findByToken(expiredToken.getToken()))
                .thenReturn(Optional.of(expiredToken));

        assertThrows(RefreshTokenExpiredException.class,
                () -> refreshTokenService.verifyRefreshToken(expiredToken.getToken()));
        
        verify(refreshTokenRepository, times(1)).delete(expiredToken);
    }

    @Test
    void testDeleteByUserId() {
        UUID userId = UUID.randomUUID();
        
        refreshTokenService.deleteByUserId(userId);
        
        verify(refreshTokenRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    void testDeleteExpiredTokens() {
        refreshTokenService.deleteExpiredTokens();
        
        verify(refreshTokenRepository, times(1)).deleteByExpiryDateBefore(any(Instant.class));
    }
}