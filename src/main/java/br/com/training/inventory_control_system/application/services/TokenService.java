package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.requests.LoginRequest;
import br.com.training.inventory_control_system.domain.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenService(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String login(LoginRequest request) {
        var user = userRepository.findByUserName(request.userName());

        if(user.isEmpty() || !user.get().isLoginCorrect(request, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("User or password is invalid!");
        }

        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("Inventory Control System")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(300L))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
