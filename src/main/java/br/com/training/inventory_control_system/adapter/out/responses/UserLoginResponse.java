package br.com.training.inventory_control_system.adapter.out.responses;

public record UserLoginResponse(
        String accessToken, 
        String refreshToken, 
        Long expiresIn,
        Long refreshExpiresIn,
        String tokenType
) {
    public UserLoginResponse(String accessToken, String refreshToken, Long expiresIn, Long refreshExpiresIn) {
        this(accessToken, refreshToken, expiresIn, refreshExpiresIn, "Bearer");
    }
}
