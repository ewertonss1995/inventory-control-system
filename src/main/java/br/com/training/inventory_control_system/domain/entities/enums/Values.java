package br.com.training.inventory_control_system.domain.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Values {

    ADMIN(1L),

    BASIC(2L);

    long roleId;
}
