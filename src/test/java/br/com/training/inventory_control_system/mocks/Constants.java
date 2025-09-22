package br.com.training.inventory_control_system.mocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Constants {
    public static final String NAME_MOCK = "Product test";
    public static final String DESCRIPTION_MOCK = "Product Description";
    public static final BigDecimal UNIT_PRICE_MOCK = BigDecimal.valueOf(10.00);
    public static final int QUANTITY_MOCK = 10;
    public static final String CATEGORY_NAME_MOCK = "Product category";
    public static final int CATEGORY_ID_MOCK = 1;
    public static final LocalDateTime DATE_NOW_MOCK = LocalDateTime.now();
    public static final int ID_MOCK = 1;
    public static final BigDecimal TOTAL_PRICE_MOCK = BigDecimal.valueOf(100.00);
    public static final String USER_MOCK = "user mock";
    public static final String PASSWORD_MOCK = "123456";
    public static final UUID UUID_MOCK = UUID.randomUUID();
    public static final String ROLE_ADMIN_MOCK = "ADMIN";
    public static final String ROLE_BASIC_MOCK = "BASIC";
}
