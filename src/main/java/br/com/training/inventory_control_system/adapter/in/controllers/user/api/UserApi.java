package br.com.training.inventory_control_system.adapter.in.controllers.user.api;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UpdateUserRequest;
import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.UserLoginResponse;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "UserController", description = "Operations for managing users")
public interface UserApi {

    @Operation(summary = "Updates an existing user",
            description = "Updates the details of a user by ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data that will be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = br.com.training.inventory_control_system.adapter.out.responses.ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"The body of the request is empty or invalid. Please provide valid data.\",\n" +
                                                    "    \"status\": 400\n" +
                                                    "}"
                                    )
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"You do not have permission to delete an administrator user.\",\n" +
                                                    "    \"status\": 403\n" +
                                                    "}")
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error during execution: User with ID 1 was not found: Error.\",\n" +
                                                    "    \"status\": 404\n" +
                                                    "}")
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error communicating with the database: Error\",\n" +
                                                    "    \"status\": 500\n" +
                                                    "}")
                            )
                    )
            })
    ResponseEntity<br.com.training.inventory_control_system.adapter.out.responses.ApiResponse> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable UUID userId,
            @Parameter(description = "Request to update the user")
            @RequestBody @Valid UpdateUserRequest request);


    @Operation(summary = "User login", description = "Authenticate the user and return a login response.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data that will be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"Invalid user: User or password is invalid!\",\n" +
                                            "    \"status\": 401\n" +
                                            "}")
                    )
            )
    })
    ResponseEntity<UserLoginResponse> userLogin(
            @Parameter(description = "User login request containing username and password")
            @Valid @RequestBody UserRequest request);

    @Operation(summary = "Create a new user", description = "Register a new user in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data that will be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"The body of the request is empty or invalid. Please provide valid data.\",\n" +
                                            "    \"status\": 400\n" +
                                            "}"
                            )
                    ))
    })
    void createUser(@Parameter(description = "User registration request") @Valid @RequestBody UserRequest request);

    @Operation(summary = "Create a new admin user", description = "Register a new admin user in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Admin user data that will be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"The body of the request is empty or invalid. Please provide valid data.\",\n" +
                                            "    \"status\": 400\n" +
                                            "}"
                            )
                    )
            )
    })
    @Hidden
    void createAdminUser(
            @Parameter(description = "Admin user registration request")
            @Valid @RequestBody UserRequest request);

    @Operation(summary = "Get user by Id", description = "Retrieve a user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"User is not authorized to make this request: Access Denied\",\n" +
                                            "    \"status\": 403\n" +
                                            "}"
                            )
                    ))
    })
    ResponseEntity<List<UserResponse>> getUsers();

    @Operation(summary = "Retrieves a user by ID",
            description = "Fetches the details of a user specified by the ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error during execution: user with ID 1 was not found.\",\n" +
                                                    "    \"status\": 404\n" +
                                                    "}"
                                    )
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error communicating with the database: Error\",\n" +
                                                    "    \"status\": 500\n" +
                                                    "}"
                                    )
                            ))
            })
    ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable UUID userId);

    @Operation(summary = "Deletes a user",
            description = "Removes a user from the system by specifying the ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"You do not have permission to delete an administrator user.\",\n" +
                                            "    \"status\": 403\n" +
                                            "}")
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"Error during execution: User with ID 1 was not found: Error.\",\n" +
                                            "    \"status\": 404\n" +
                                            "}")
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"Error communicating with the database: Error\",\n" +
                                            "    \"status\": 500\n" +
                                            "}")
                    ))
    })
    void deleteUser(@Parameter(description = "ID of the user to delete", required = true) @PathVariable UUID userId);
}
