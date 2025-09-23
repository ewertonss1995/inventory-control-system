package br.com.training.inventory_control_system.adapter.in.controllers.category.api;

import br.com.training.inventory_control_system.adapter.in.controllers.category.request.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "CategoryController", description = "Operations for managing categories")
public interface CategoryApi {

    @Operation(summary = "Creates a new category",
            description = "Adds a new category to the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category data that will be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                            "    \"message\": \"The body of the request is empty or invalid. Please provide valid data.\",\n" +
                                            "    \"status\": 404\n" +
                                            "}")
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error communicating with the database: Error\",\n" +
                                                    "    \"status\": 500\n" +
                                                    "}"
                                    )
                            ))
            })
    ResponseEntity<ApiResponse> saveCategory(
            @Parameter(description = "Request to create a new category")
            @RequestBody @Valid CategoryRequest request
    );

    @Operation(summary = "Retrieves a category by ID",
            description = "Fetches the details of a category specified by the ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetCategoryResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error during execution: Category with ID 1 was not found.\",\n" +
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
    ResponseEntity<GetCategoryResponse> getCategory(
            @Parameter(description = "ID of the category to retrieve", required = true)
            @PathVariable Integer categoryId
    );

    @Operation(summary = "Retrieves all categories",
            description = "Fetches a list of all categories.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetCategoryResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error during execution: Unable to get categories: Error.\",\n" +
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
    ResponseEntity<List<GetCategoryResponse>> getCategories();

    @Operation(summary = "Updates an existing category",
            description = "Updates the details of a category by ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error during execution: Category with ID 1 was not found: Error.\",\n" +
                                                    "    \"status\": 404\n" +
                                                    "}")
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"The body of the request is empty or invalid. Please provide valid data.\",\n" +
                                                    "    \"status\": 404\n" +
                                                    "}"
                                    )
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
    ResponseEntity<ApiResponse> updateCategory(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable Integer categoryId,
            @Parameter(description = "Request to update the category")
            @RequestBody @Valid CategoryRequest request
    );

    @Operation(summary = "Deletes a category",
            description = "Removes a category from the system by specifying the ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found.",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"Error during execution: Category with ID 1 was not found: Error.\",\n" +
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
    void deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true)
            @PathVariable Integer categoryId
    );
}
