package com.yaroslav.booknetwork.config;

import lombok.RequiredArgsConstructor; // Автогенерация конструктора для всех финальных полей
import org.springframework.context.annotation.Bean; // Создание и управление бинами Spring
import org.springframework.context.annotation.Configuration; // Указывает, что класс содержит конфигурацию Spring
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders; // Константы для заголовков HTTP
import org.springframework.security.authentication.AuthenticationManager; // Менеджер аутентификации
import org.springframework.security.authentication.AuthenticationProvider; // Поставщик аутентификации
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Реализация аутентификации, использующая UserDetailsService и PasswordEncoder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Конфигурация аутентификации
import org.springframework.security.core.userdetails.UserDetailsService; // Интерфейс для загрузки деталей пользователя
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Реализация шифрования паролей с использованием алгоритма bcrypt
import org.springframework.security.crypto.password.PasswordEncoder; // Интерфейс для шифрования паролей
import org.springframework.web.cors.CorsConfiguration; // Конфигурация CORS (Cross-Origin Resource Sharing)
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Источник конфигурации CORS
import org.springframework.web.filter.CorsFilter; // Фильтр для обработки запросов CORS

import java.util.Arrays; // Утилиты для работы с массивами
import java.util.Collections; // Утилиты для работы с коллекциями
import java.util.List;

@Configuration // Класс содержит конфигурацию Spring
@RequiredArgsConstructor // Создает конструктор с параметрами для всех финальных полей
public class BeansConfig {

    // Поле для UserDetailsService, используемого при аутентификации пользователей
    private final UserDetailsService userDetailsService;

    /**
     * Создает бин AuthenticationProvider, который используется для аутентификации пользователей.
     * В данной реализации используется DaoAuthenticationProvider, который требует UserDetailsService и PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); // Использует UserDetailsService и PasswordEncoder
        authenticationProvider.setUserDetailsService(userDetailsService); // Устанавливает сервис для загрузки деталей пользователя
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Устанавливает метод шифрования паролей
        return authenticationProvider; // Возвращает готовый AuthenticationProvider
    }

    /**
     * Создает бин AuthenticationManager, который управляет процессом аутентификации.
     * Получает менеджера аутентификации из конфигурации.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Возвращает менеджера аутентификации
    }

    @Bean
    public AuditorAware<Integer> auditorAware(){
        return new ApplicationAuditAware();
    }
    /**
     * Создает бин PasswordEncoder, который используется для шифрования и сравнения паролей.
     * В данном случае используется BCryptPasswordEncoder, который применяет алгоритм bcrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Возвращает реализацию для шифрования паролей
    }

    /**
     * Создает бин CorsFilter для управления кросс-доменными запросами.
     * Определяет конфигурацию CORS, включая разрешенные заголовки, методы и источники запросов.
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Источник конфигурации CORS
        final CorsConfiguration configuration = new CorsConfiguration(); // Конфигурация CORS

        // Устанавливает параметры конфигурации CORS
        configuration.setAllowCredentials(true); // Разрешает отправку кук
        configuration.setAllowedOrigins(List.of("http://localhost:8080","http://localhost:4200")); // Разрешенные источники
        configuration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        )); // Разрешенные заголовки
        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "DELETE",
                "PUT",
                "PATCH"
        )); // Разрешенные методы

        // Регистрирует конфигурацию CORS для всех путей
        source.registerCorsConfiguration("/**", configuration);

        // Возвращает новый CorsFilter с установленной конфигурацией
        return new CorsFilter(source);
    }
}
