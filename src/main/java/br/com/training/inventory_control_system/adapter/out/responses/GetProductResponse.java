package br.com.training.inventory_control_system.adapter.out.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {

    private Integer productId;

    private String productName;

    private String productDescription;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String category;

    private LocalDateTime registrationDate;

    private LocalDateTime updateDate;
}
