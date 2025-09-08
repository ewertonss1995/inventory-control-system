package br.com.training.inventory_control_system.adapter.out.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryResponse {

    private Integer categoryId;

    private String categoryName;

    private LocalDateTime registrationDate;

    private LocalDateTime updateDate;
}
