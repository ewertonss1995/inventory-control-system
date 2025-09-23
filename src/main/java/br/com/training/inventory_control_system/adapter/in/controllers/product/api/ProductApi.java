package br.com.training.inventory_control_system.adapter.in.controllers.product.api;

import br.com.training.inventory_control_system.adapter.in.controllers.product.request.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.ApiResponse;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "ProductController", description = "Operations for managing products")
public interface ProductApi {

    @Operation(summary = "Creates a new product",
            description = "Adds a new product to the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product data that will be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequest.class)
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
                                                    "    \"status\": 400\n" +
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
    ResponseEntity<ApiResponse> saveProduct(
            @Parameter(description = "Request to create a new product")
            @RequestBody @Valid ProductRequest request
    );

    @Operation(summary = "Retrieves a product by ID",
            description = "Fetches the details of a product specified by the ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetProductResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error during execution: Product with ID 1 was not found.\",\n" +
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
    ResponseEntity<GetProductResponse> getProduct(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable Integer productId
    );

    @Operation(summary = "Retrieves all products",
            description = "Fetches a list of all products.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GetProductResponse.class))
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"Error during execution: Unable to update products: Error.\",\n" +
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
    ResponseEntity<List<GetProductResponse>> getProducts();

    @Operation(summary = "Updates an existing product",
            description = "Updates the details of a product by ID.",
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
                                                    "    \"message\": \"Error during execution: Product with ID 1 was not found: Error.\",\n" +
                                                    "    \"status\": 404\n" +
                                                    "}")
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "    \"message\": \"The body of the request is empty or invalid. Please provide valid data.\",\n" +
                                                    "    \"status\": 400\n" +
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
    ResponseEntity<ApiResponse> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Integer productId,
            @Parameter(description = "Request to update the product")
            @RequestBody @Valid ProductRequest request
    );

    @Operation(summary = "Deletes a product",
            description = "Removes a product from the system by specifying the ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found.",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "    \"message\": \"Error during execution: Product with ID 1 was not found: Error.\",\n" +
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
    void deletarProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Integer productId
    );
}
