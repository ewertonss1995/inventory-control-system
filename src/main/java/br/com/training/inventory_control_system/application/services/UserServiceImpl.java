package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UpdateUserRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.UserMapper;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.product.ProductNotFoundException;
import br.com.training.inventory_control_system.application.exception.user.UserNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Role;
import br.com.training.inventory_control_system.domain.entities.User;
import br.com.training.inventory_control_system.domain.entities.enums.RolesEnum;
import br.com.training.inventory_control_system.domain.repositories.RoleRepository;
import br.com.training.inventory_control_system.domain.repositories.UserRepository;
import br.com.training.inventory_control_system.port.in.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(JwtEncoder jwtEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public String userLogin(UserRequest request) {
        var user = userRepository.findByUserName(request.userName());

        if (user.isEmpty() || !user.get().isLoginCorrect(request, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("User or password is invalid!");
        }

        var now = Instant.now();

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("Inventory Control System")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(300L))
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
            throw new IllegalArgumentException("You do not have permission to change an administrator user's information.");
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
