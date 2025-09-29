package br.com.training.inventory_control_system.adapter.in.controllers.user.api;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UpdateUserRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserLoginResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.port.in.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Validated
public class UserController implements UserApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> userLogin(@Valid @RequestBody UserRequest request) {
        return ResponseEntity
                .ok(new UserLoginResponse(userService.userLogin(request), 300L));
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public void createUser(@Valid @RequestBody UserRequest request) {
        userService.createUser(request);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void createAdminUser(@Valid @RequestBody UserRequest request) {
        userService.createAdminUser(request);
    }

    @Override
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Override
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest request) {

        userService.updateUser(userId, request);

        return ResponseEntity.ok(ApiResponse.builder()
                .message(String.format("Update successful for user id %s.", userId))
                .status(HttpStatus.OK.value())
                .build());
    }

    @Override
    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}
