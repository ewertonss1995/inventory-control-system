package br.com.training.inventory_control_system.adapter.in.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank
    @Size(max = 50)
    private String productName;

    @NotBlank
    @Size(max = 100)
    private String productDescription;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private Integer quantity;

    @NotBlank
    @Size(max = 50)
    private String category;

    private LocalDateTime registrationDate;

    private LocalDateTime removeDate;

}
