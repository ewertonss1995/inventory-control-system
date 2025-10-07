package br.com.training.inventory_control_system.config;

import br.com.training.inventory_control_system.application.services.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingConfig.class);
    
    private final RefreshTokenService refreshTokenService;

    public SchedulingConfig(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Clean up expired refresh tokens every hour
     */
    @Scheduled(fixedRate = 3600000) // 1 hour = 3,600,000 milliseconds
    public void cleanupExpiredRefreshTokens() {
        LOGGER.info("[SchedulingConfig] - Starting cleanup of expired refresh tokens");
        refreshTokenService.deleteExpiredTokens();
        LOGGER.info("[SchedulingConfig] - Cleanup of expired refresh tokens completed");
    }
}