package ru.skypro.homework.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI (Swagger) для документирования API.
 *
 * <p>Настраивает спецификацию Swagger UI, добавляя поддержку
 * схемы авторизации {@code Basic Auth}. Это позволяет тестировать
 * защищенные эндпоинты в браузере, используя кнопку <b>Authorize</b>.</p>
 *
 * <p>Настройка необходима для корректной передачи заголовков авторизации
 * при работе приложения в Docker-контейнерах и в Swagger.</p>
 *
 * @see ru.skypro.homework.config.WebSecurityConfig
 */

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "basicAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }


}
