package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.domain.entities.Role;
import br.com.training.inventory_control_system.domain.entities.User;
import br.com.training.inventory_control_system.domain.entities.enums.RolesEnum;
import br.com.training.inventory_control_system.domain.repositories.RoleRepository;
import br.com.training.inventory_control_system.domain.repositories.UserRepository;
import br.com.training.inventory_control_system.port.in.UserService;
import jakarta.transaction.Transactional;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(JwtEncoder jwtEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public String userLogin(UserRequest request) {
        var user = userRepository.findByUserName(request.userName());

        if(user.isEmpty() || !user.get().isLoginCorrect(request, bCryptPasswordEncoder)) {
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
        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var basicRole = roleRepository.findByName(RolesEnum.BASIC.name());

        var user = requestToEntity(request, Set.of(basicRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void createAdminUser(UserRequest request) {
        var adminRole = roleRepository.findByName(RolesEnum.ADMIN.name());
        var basicRole = roleRepository.findByName(RolesEnum.BASIC.name());

        var userAdmin = userRepository.findByUserName("admin");

        if(userAdmin.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = requestToEntity(request, Set.of(basicRole, adminRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    private User requestToEntity(UserRequest request, Set<Role> roles) {
        User user = new User();
        user.setUserName(request.userName());
        user.setPassword(request.password());
        user.setRoles(roles);
        return user;
    }
}
