package com.yaroslav.booknetwork.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull; // Lombok аннотация для проверки на null
import lombok.RequiredArgsConstructor; // Lombok аннотация для автогенерации конструктора с финальными полями
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Токен аутентификации
import org.springframework.security.core.context.SecurityContextHolder; // Контекст безопасности Spring Security
import org.springframework.security.core.userdetails.UserDetails; // Детали пользователя
import org.springframework.security.core.userdetails.UserDetailsService; // Сервис для загрузки деталей пользователя
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Информация для аутентификации
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service; // Аннотация для объявления класса сервисом Spring
import org.springframework.web.filter.OncePerRequestFilter; // Фильтр, который выполняется один раз за запрос

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION; // Постоянная для заголовка авторизации

//@Component // Объявляет класс сервисом Spring
@RequiredArgsConstructor // Автоматически генерирует конструктор для всех финальных полей
public class JwtFilter {//extends OncePerRequestFilter { // Наследует от фильтра, который применяется один раз за запрос

//
//    // Два финальных поля для необходимых зависимостей
//    private final JwtService jwtService; // Сервис для работы с JWT-токенами
//    private final UserDetailsService userDetailsService; // Сервис для загрузки деталей пользователя
//
//    // Переопределяет метод, который выполняется каждый раз при фильтрации запросов
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request, // HTTP-запрос от клиента
//            @NonNull HttpServletResponse response, // HTTP-ответ для клиента
//            @NonNull FilterChain filterChain // Цепочка фильтров
//    ) throws ServletException, IOException {
//
//        try {
//            // Использует точное сравнение или регулярное выражение для точной проверки
//            if ("/api/v1/auth".equals(request.getServletPath())) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            // Получает заголовок авторизации
//            final String authHeader = request.getHeader(AUTHORIZATION);
//            final String jwt;
//            final String userEmail;
//
//            // Проверяет, что заголовок не пустой и начинается с "Bearer "
//            if (authHeader == null || authHeader.trim().isEmpty() || !authHeader.startsWith("Bearer ")) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            // Проверяет длину заголовка перед извлечением JWT
//            if (authHeader.length() < 8) { // "Bearer " + минимум один символ
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            // Извлекает JWT-токен из заголовка
//            jwt = authHeader.substring(7);
//            userEmail = jwtService.extractUserName(jwt); // Извлекает email из JWT-токена
//
//            // Если email не null и контекст не содержит аутентификации, пытается аутентифицировать
//            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); // Загружает детали пользователя
//                if (jwtService.isTokenValid(jwt, userDetails)) { // Проверяет валидность токена
//                    // Создает токен аутентификации
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails, // Пользовательские данные
//                            null, // Нет пароля
//                            userDetails.getAuthorities() // Полномочия
//                    );
//                    authToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request) // Детали аутентификации
//                    );
//                    // Устанавливает аутентификацию в контекст безопасности
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//        } catch (IllegalArgumentException e) {
//            // Обработка исключения при неверном аргументе (например, некорректный JWT)
//            System.err.println("Invalid argument: " + e.getMessage());
//        } catch (UsernameNotFoundException e) {
//            // Обработка исключения, если пользователь не найден
//            System.err.println("User not found: " + e.getMessage());
//        } catch (Exception e) {
//            // Общий блок для всех других исключений
//            System.err.println("An error occurred during authentication: " + e.getMessage());
//        }
//        filterChain.doFilter(request, response);
//    }
}
