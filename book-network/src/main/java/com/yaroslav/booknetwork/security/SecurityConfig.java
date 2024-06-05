package com.yaroslav.booknetwork.security;

// Аннотации указывают, что этот класс предназначен для конфигурации Spring Security
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Импорт статических методов для настройки конфигурации
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

// Класс конфигурации безопасности
@Configuration // Указывает, что этот класс используется для настройки Spring
@EnableWebSecurity // Включает функции безопасности для веб-приложения
@RequiredArgsConstructor // Автоматически генерирует конструктор для необходимых зависимостей
@EnableMethodSecurity(securedEnabled = true) // Включает безопасность на уровне методов
public class SecurityConfig {

    // Два финальных поля для зависимостей, внедренных через конструктор
//    private final JwtFilter jwtAuthFilter; // Фильтр для JWT аутентификации
//    private final AuthenticationProvider authenticationProvider; // Провайдер аутентификации

    // Метод, создающий цепочку фильтров безопасности
    @Bean // Аннотация, указывающая, что этот метод создает Spring Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Включение CORS (межсайтового обмена данными)
                .cors(withDefaults())
                // Отключение CSRF (защиты от межсайтовых подделок запросов)
                .csrf(AbstractHttpConfigurer::disable)
                // Настройка авторизации запросов
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/auth/**", // URL-шаблоны, которые не требуют авторизации
                                        "/v2/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/swagger-ui/**"
                                ).permitAll() // Разрешить доступ без авторизации
                                .anyRequest() // Все остальные запросы
                                .authenticated() // Требуют авторизации
                )
                // Настройка управления сессиями
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(STATELESS) // Устанавливает режим без сессий
//                )
//                // Установка провайдера аутентификации
//                .authenticationProvider(authenticationProvider)
//                // Добавление JWT фильтра перед стандартным фильтром аутентификации
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                .oauth2ResourceServer(auth ->
                        auth.jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));

        return http.build(); // Возвращает созданную цепочку фильтров
    }
}
