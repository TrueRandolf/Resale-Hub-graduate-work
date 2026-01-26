package ru.skypro.homework.config;

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
 * <p>CORS настроен для работы браузера с портами 3000 и 8080 (для работы в Docker).</p>
 *
 * @see ru.skypro.homework.config.OpenApiConfig
 * @see ru.skypro.homework.config.WebConfig
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login",
            "/register",
            "/images/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(withDefaults())
                .authorizeHttpRequests((authorization) -> authorization
                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                        .mvcMatchers("/ads/**", "/users/**").authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
