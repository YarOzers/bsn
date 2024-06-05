package com.yaroslav.booknetwork.config;

import org.springframework.data.domain.AuditorAware; // Интерфейс AuditorAware предоставляет способ определить текущего аудитора для аудита данных
import org.springframework.security.core.context.SecurityContextHolder; // Хранит контекст безопасности, включающий данные о текущем пользователе
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component; // Аннотация, указывающая, что класс является Spring-компонентом

import java.util.Optional; // Удобный способ представления значения, которое может быть или отсутствовать

/**
 * ApplicationAuditAware реализует AuditorAware<Integer>, чтобы определять текущего аудитора
 * для целей аудита в Spring Data JPA. Это позволяет автоматически записывать, какой пользователь
 * совершил определенные действия, например, создание или изменение записи в базе данных.
 */

@Component // Указывает, что этот класс является компонентом Spring и может быть внедрен в другие части приложения
public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(securityContext -> {
                    if (securityContext.getAuthentication() == null) {
                        return null;
                    }
                    Object principal = securityContext.getAuthentication().getPrincipal();
                    if (principal instanceof Jwt jwt) {
                        // Извлечение идентификатора пользователя из токена JWT
                        return jwt.getClaimAsString("preferred_username"); // Или другой ключ, используемый для идентификации
                    } else if (principal instanceof UserDetails) {
                        return ((UserDetails) principal).getUsername();
                    } else {
                        return principal.toString();
                    }
                });
    }
}
