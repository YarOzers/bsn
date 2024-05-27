package com.yaroslav.booknetwork.common;

// Импорт необходимых классов и аннотаций

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*; // JPA-аннотации для работы с ORM (Object-Relational Mapping)
import jakarta.persistence.Id;
import lombok.*; // Lombok-аннотации для автоматического создания кода
import lombok.experimental.FieldDefaults; // Позволяет установить уровень доступа к полям
import lombok.experimental.SuperBuilder; // Позволяет использовать паттерн "строитель" для создания объектов
import org.springframework.data.annotation.*; // Аннотации Spring Data для аудита
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // Слушатель событий для аудита

import java.time.LocalDateTime; // Для работы с локальными датами и временем

/**
 * Базовый класс, представляющий сущности, которые содержат общие поля.
 * Используется как суперкласс для других сущностей.
 * Включает свойства для уникального идентификатора, временных меток создания и последнего изменения,
 * а также информацию о пользователях, которые создали или последний раз изменили сущность.
 */
@Getter // Lombok автоматически создает методы "геттеры" для всех полей
@Setter // Lombok автоматически создает методы "сеттеры" для всех полей
@SuperBuilder // Позволяет использовать паттерн "строитель" для создания объектов
@AllArgsConstructor // Lombok создает конструктор, который принимает все параметры
@NoArgsConstructor // Lombok создает конструктор по умолчанию (без параметров)
@MappedSuperclass
// Указывает, что класс является суперклассом, который не отображается в базе данных напрямую, но может использоваться в других сущностях
@EntityListeners(AuditingEntityListener.class) // Добавляет слушатель для поддержки аудита
@FieldDefaults(level = AccessLevel.PRIVATE) // Устанавливает уровень доступа к полям как "private"
public class BaseEntity { // Базовый класс для других сущностей

    // Уникальный идентификатор сущности
    @Id // Указывает, что это первичный ключ
    @GeneratedValue // Значение генерируется автоматически
            Integer id; // Уникальный идентификатор сущности

    // Дата и время создания сущности
    @CreatedDate // Аннотация Spring Data, указывающая, что поле хранит дату создания
    @Column(nullable = false, updatable = false) // Не может быть null и не может быть обновлено после создания
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime createdDate; // Дата и время создания сущности

    // Дата и время последнего изменения сущности
    @LastModifiedDate // Аннотация Spring Data, указывающая, что поле хранит дату последнего изменения
    @Column(insertable = false) // Поле не может быть установлено вручную при создании сущности
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime lastModifiedDate; // Дата и время последнего изменения сущности

    // Пользователь, который создал сущность
    @CreatedBy // Аннотация Spring Data, указывающая, что это поле хранит идентификатор создателя сущности
    @Column(nullable = false, updatable = false) // Поле не может быть null и не может быть обновлено
            Integer createdBy; // Идентификатор пользователя, который создал сущность

    // Пользователь, который последний раз изменил сущность
    @LastModifiedBy // Аннотация Spring Data, указывающая, что это поле хранит идентификатор последнего модификатора
    @Column(updatable = false) // Поле не может быть обновлено после создания сущности
            Integer lastModifiedBy; // Идентификатор пользователя, который последний раз изменил сущность
}
