package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация безопасности веб-приложения.
 *
 * <p>Класс отвечает за настройку прав доступа, фильтрацию HTTP-запросов
 * и политику (CORS). В текущей конфигурации используется аутентификация {@code Basic Auth}.</p>
 *
 * <p><b>Основные настройки:</b></p>
 * <ul>
 *     <li>Отключена защита CSRF для упрощения взаимодействия с REST API.</li>
 *     <li>Сформирован "белый список" ({@code AUTH_WHITELIST}), доступный без авторизации,
 *         включая ресурсы Swagger UI и эндпоинты регистрации/авторизации.</li>
 *     <li>Настроен CORS для одновременной работы фронтенд-приложения (порт 3000)
 *         и Swagger (порт 8080).</li>
 * </ul>
 *
 * <p>Конфигурация полностью вынесена в параметры приложения (YAML) для
 * гибкости настройки CORS и прав доступа для различных окружений.</p>
 *
 * @see ru.skypro.homework.config.OpenApiConfig
 * @see ru.skypro.homework.config.WebConfig
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${app.security.whitelist}")
    private String[] authWhitelist;

    @Value("${app.security.protected-endpoints}")
    private String[] protectedEndpoints;

    @Value("${app.security.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${app.security.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${app.security.allowed-headers}")
    private List<String> allowedHeaders;

    @Value("${app.security.cors-mapping}")
    private String corsMapping;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(withDefaults())
                .authorizeHttpRequests((authorization) -> authorization
                        .mvcMatchers(authWhitelist).permitAll()
                        .mvcMatchers(protectedEndpoints).authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsMapping, configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
