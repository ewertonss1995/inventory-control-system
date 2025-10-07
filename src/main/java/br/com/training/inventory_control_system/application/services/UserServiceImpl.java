package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.RefreshTokenRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UpdateUserRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.UserMapper;
import br.com.training.inventory_control_system.adapter.out.responses.RefreshTokenResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserLoginResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.product.ProductNotFoundException;
import br.com.training.inventory_control_system.application.exception.user.UserNotFoundException;
import br.com.training.inventory_control_system.domain.entities.RefreshToken;
import br.com.training.inventory_control_system.domain.entities.Role;
import br.com.training.inventory_control_system.domain.entities.User;
import br.com.training.inventory_control_system.domain.entities.enums.RolesEnum;
import br.com.training.inventory_control_system.domain.repositories.RoleRepository;
import br.com.training.inventory_control_system.domain.repositories.UserRepository;
import br.com.training.inventory_control_system.port.in.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${app.jwt.expiration:#{5*60}}")
    private Long jwtExpirationInSeconds; // 5 minutes default

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;

    public UserServiceImpl(JwtEncoder jwtEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, UserMapper userMapper,
                           RefreshTokenService refreshTokenService) {
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    @Override
    public UserLoginResponse userLogin(UserRequest request) {
        var user = userRepository.findByUserName(request.userName())
                .or(() -> userRepository.findByUserEmail(request.userEmail()));

        if (user.isEmpty() || !user.get().isLoginCorrect(request, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("User or password is invalid!");
        }

        User authenticatedUser = user.get();
        
        // Generate access token
        String accessToken = generateAccessToken(authenticatedUser);
        
        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);
        
        LOGGER.info("[UserServiceImpl] - User logged in successfully: {}", authenticatedUser.getUserId());
        
        return new UserLoginResponse(
                accessToken,
                refreshToken.getToken(),
                jwtExpirationInSeconds,
                refreshTokenService.getRefreshTokenDurationInSeconds()
        );
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken());
        User user = refreshToken.getUser();
        
        // Generate new access token
        String newAccessToken = generateAccessToken(user);
        
        // Generate new refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        
        LOGGER.info("[UserServiceImpl] - Token refreshed for user: {}", user.getUserId());
        
        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                jwtExpirationInSeconds,
                refreshTokenService.getRefreshTokenDurationInSeconds()
        );
    }

    @Override
    public void logout(UUID userId) {
        refreshTokenService.deleteByUserId(userId);
        LOGGER.info("[UserServiceImpl] - User logged out: {}", userId);
    }

    private String generateAccessToken(User user) {
        var now = Instant.now();

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("Inventory Control System")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpirationInSeconds))
                .claim("scope", scopes)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Transactional
    @Override
    public void createUser(UserRequest request) {
        var userFromDb = userRepository.findByUserName(request.userName());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var basicRole = roleRepository.findByName(RolesEnum.BASIC.name());

        var user = userMapper.toEntity(request);
        user.setRoles(Set.of(basicRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void createAdminUser(UserRequest request) {
        var adminRole = roleRepository.findByName(RolesEnum.ADMIN.name());
        var basicRole = roleRepository.findByName(RolesEnum.BASIC.name());

        var userAdmin = userRepository.findByUserName("admin");

        if (userAdmin.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = userMapper.toEntity(request);
        user.setRoles(Set.of(basicRole, adminRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.toUserResponseList(userList);
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with ID %s was not found.", userId)));

        return userMapper.toUserResponse(user);
    }

    @Override
    public void updateUser(UUID userId, UpdateUserRequest request) {
        User entity = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException(
                        String.format("User with ID %s was not found.", userId)));

        userMapper.updateEntityFromRequest(request, entity);

        if (entity.getRoles().contains(new Role(1, RolesEnum.ADMIN.name()))) {
            throw new IllegalArgumentException("You do not have permission to update an administrator user's information.");
        }

        if (request.password() != null && !request.password().isBlank()) {
            entity.setPassword(passwordEncoder.encode(request.password()));
        }

        userRepository.save(entity);

        LOGGER.info("[UserServiceImpl] - User with ID: {} was updated successfully.", userId);
    }

    @Override
    public void deleteUser(UUID userId) {
        User entity = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException(
                        String.format("User with ID %s was not found.", userId)));

        if (entity.getRoles().contains(new Role(1, RolesEnum.ADMIN.name()))) {
            throw new IllegalArgumentException("You do not have permission to delete an administrator user.");
        }

        userRepository.delete(entity);
        LOGGER.info("[UserServiceImpl] - User with ID: {} was deleted successfully.", userId);
    }
}
