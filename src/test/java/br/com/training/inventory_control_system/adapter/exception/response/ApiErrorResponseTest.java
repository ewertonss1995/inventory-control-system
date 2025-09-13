package br.com.training.inventory_control_system.adapter.exception.response;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
class ApiErrorResponseTest {

    @Test
    public void testApiErrorResponseBuilder() {
        int status = HttpStatus.NOT_FOUND.value();
        String message = "Not Found";

        ApiErrorResponse response = ApiErrorResponse.builder()
                .message(message)
                .status(status)
                .build();

        assertEquals(message, response.getMessage());
        assertEquals(status, response.getStatus());
    }

    @Test
    public void testSetMessage() {
        String message = "Internal Server Error";
        ApiErrorResponse response = ApiErrorResponse.builder().message(message).build();
        assertEquals(message, response.getMessage());
    }

    @Test
    public void testSetStatus() {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(status)
                .build();

        assertEquals(status, response.getStatus());
    }

    @Test
    public void testDataEquals() {
        String message = "Forbidden";
        int status = HttpStatus.FORBIDDEN.value();

        ApiErrorResponse response1 = ApiErrorResponse.builder()
                .message(message)
                .status(status)
                .build();

        ApiErrorResponse response2 = ApiErrorResponse.builder()
                .message(message)
                .status(status)
                .build();

        assertEquals(response1, response2);
    }

    @Test
    public void testDataHashCode() {
        String message = "Unauthorized";
        int status = HttpStatus.UNAUTHORIZED.value();

        ApiErrorResponse response1 = ApiErrorResponse.builder()
                .message(message)
                .status(status)
                .build();

        ApiErrorResponse response2 = ApiErrorResponse.builder()
                .message(message)
                .status(status)
                .build();

        assertEquals(response1.hashCode(), response2.hashCode());
    }
}
