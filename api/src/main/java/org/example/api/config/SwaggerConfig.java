package org.example.api.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "basicAuth", scheme = "basic")
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Design Итоговое задание")
                        .description("Документация по API, разработанному в ходе выполнения итогового задания")
                        .version("1.0-SNAPSHOT")
                        .contact(
                                new Contact()
                                        .name("Александр Климчук")
                                        .email("klimchuck.alexander@gmail.com")
                                        .url("https://t.me/klimchuk_av")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Сервер для выполнения запросов")
                ));
    }
}
