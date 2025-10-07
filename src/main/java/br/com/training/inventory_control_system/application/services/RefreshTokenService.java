package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.application.exception.user.RefreshTokenExpiredException;
import br.com.training.inventory_control_system.application.exception.user.RefreshTokenNotFoundException;
import br.com.training.inventory_control_system.domain.entities.RefreshToken;
import br.com.training.inventory_control_system.domain.entities.User;
import br.com.training.inventory_control_system.domain.repositories.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);
    
    @Value("${app.jwt.refresh-expiration:#{7*24*60*60}}")
    private Long refreshTokenDurationInSeconds; // 7 days default
    
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // Delete any existing refresh token for the user
        refreshTokenRepository.deleteByUser(user);
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationInSeconds));
        
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        
        LOGGER.info("[RefreshTokenService] - Refresh token created for user: {}", user.getUserId());
        
        return savedToken;
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
        
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException("Refresh token expired. Please login again.");
        }
        
        return refreshToken;
    }

    @Transactional
    public void deleteByUserId(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
        LOGGER.info("[RefreshTokenService] - Refresh tokens deleted for user: {}", userId);
    }

    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
        LOGGER.info("[RefreshTokenService] - Expired refresh tokens cleaned up");
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Long getRefreshTokenDurationInSeconds() {
        return refreshTokenDurationInSeconds;
    }
}