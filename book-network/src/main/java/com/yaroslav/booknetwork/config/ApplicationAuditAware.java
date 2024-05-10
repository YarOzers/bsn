package com.yaroslav.booknetwork.config;

// Импорт необходимых классов из Spring Framework и Java
import com.yaroslav.booknetwork.user.User;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.AuditorAware; // Интерфейс AuditorAware предоставляет способ определить текущего аудитора для аудита данных
import org.springframework.security.authentication.AnonymousAuthenticationToken; // Представляет анонимный токен аутентификации
import org.springframework.security.core.Authentication; // Интерфейс для информации о текущей аутентификации
import org.springframework.security.core.context.SecurityContextHolder; // Хранит контекст безопасности, включающий данные о текущем пользователе
import org.springframework.stereotype.Component; // Аннотация, указывающая, что класс является Spring-компонентом
import java.util.Optional; // Удобный способ представления значения, которое может быть или отсутствовать

/**
 * ApplicationAuditAware реализует AuditorAware<Integer>, чтобы определять текущего аудитора
 * для целей аудита в Spring Data JPA. Это позволяет автоматически записывать, какой пользователь
 * совершил определенные действия, например, создание или изменение записи в базе данных.
 */

@Component // Указывает, что этот класс является компонентом Spring и может быть внедрен в другие части приложения
public class ApplicationAuditAware implements AuditorAware<Integer> {
    Optional<Integer> auditor = Optional.empty(); // Переменная для хранения текущего аудитора

    /**
     * Метод getCurrentAuditor() возвращает идентификатор текущего аутентифицированного пользователя.
     * Если пользователь не аутентифицирован, возвращает пустой Optional. Этот метод используется
     * Spring Data JPA для определения пользователя, который совершил действие.
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Получаем текущую аутентификацию из SecurityContext

            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                // Проверяем, аутентифицирован ли пользователь, и не является ли он анонимным
                User userPrincipal = (User) authentication.getPrincipal();
                // Получаем объект User из Principal

                auditor = Optional.ofNullable(userPrincipal.getId());
                // Извлекаем идентификатор пользователя и сохраняем его как Optional
            }
        } catch (Exception e) {
            System.err.println("Error in getCurrentAuditor: " + e.getMessage());
            // В случае ошибки выводим сообщение в консоль
        }

        System.out.println("Returning auditor: " + auditor);
        // Логируем возвращаемого аудитора для отслеживания
        return auditor;
        // Возвращаем идентификатор текущего аудитора
    }
}
