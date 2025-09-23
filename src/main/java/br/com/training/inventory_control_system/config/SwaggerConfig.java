package br.com.training.inventory_control_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Invetory Control System")
                        .version("1.0.0")
                        .description("Invetory Control System Documentation."))
                .addServersItem(new Server().url("https://inventory-control-system-jext.onrender.com"));
    }
}