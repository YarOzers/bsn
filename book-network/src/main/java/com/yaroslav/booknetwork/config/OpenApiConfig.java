package com.yaroslav.booknetwork.config;

// Импортируем необходимые аннотации OpenAPI
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

// Конфигурация OpenAPI, используемая для описания документации API
@OpenAPIDefinition(
        info = @Info(
                // Устанавливаем контактное лицо для документации OpenAPI
                contact = @Contact(
                        name = "Yaroslav",  // Имя контактного лица
                        email = "ozerskiy.yaroslav@gmail.com"  // Электронная почта контактного лица
//          url = "http://..."  // Опционально: веб-сайт контактного лица
                ),
                description = "OpenApi documentation for Spring security",  // Описание API
                title = "OpenApi specification - Yaroslav",  // Заголовок документации
                version = "1.0",  // Версия документации
                // Информация о лицензии, используемой в проекте
                license = @License(
                        name = "Licence name",  // Название лицензии
                        url = "https://some-url.com"  // URL для подробностей о лицензии
                ),
                termsOfService = "Terms of service"  // Условия использования API
        ),
        // Список серверов, где хостится API
        servers = {
                @Server(
                        description = "Local ENV",  // Описание локального окружения
                        url = "http://localhost:9090/api/v1"  // URL локального окружения
                ),
                @Server(
                        description = "PROD ENV",  // Описание окружения для продакшна
                        url = "https://some-url.com"  // URL окружения для продакшна
                )
        },
        // Настройки безопасности, используемые в API
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"  // Название схемы безопасности
                )
        }
)

// Описание схемы безопасности для аутентификации JWT
@SecurityScheme(
        name = "bearerAuth",  // Название схемы безопасности
        description = "JWT auth description",  // Описание схемы безопасности
        scheme = "bearer",  // Схема безопасности (Bearer token)
        type = SecuritySchemeType.HTTP,  // Тип схемы безопасности (HTTP)
        bearerFormat = "JWT",  // Формат токена (JWT)
        in = SecuritySchemeIn.HEADER  // Где искать токен (в заголовке HTTP)
)
public class OpenApiConfig {
    // Этот класс служит для настройки конфигурации OpenAPI
    // В текущей версии он пуст, так как вся конфигурация сделана через аннотации
}

