package com.yaroslav.booknetwork.feedback;

// Импорт необходимых классов и аннотаций
import com.yaroslav.booknetwork.book.Book; // Сущность Book, представляющая книгу
import com.yaroslav.booknetwork.common.BaseEntity; // Базовая сущность, содержащая общие свойства для всех сущностей
import jakarta.persistence.*; // Аннотации JPA для управления ORM (Object-Relational Mapping)
import lombok.*; // Импорт аннотаций Lombok для сокращения написания кода
import lombok.experimental.FieldDefaults; // Позволяет установить уровень доступа к полям
import lombok.experimental.SuperBuilder; // Позволяет создавать объекты с использованием шаблона "строитель" с наследованием

/**
 * Сущность Feedback представляет отзыв о книге.
 * Наследуется от BaseEntity, что означает, что она имеет общие свойства, такие как идентификатор и временные метки.
 */
@Getter // Lombok автоматически создает методы "геттеры" для всех полей
@Setter // Lombok автоматически создает методы "сеттеры" для всех полей
@SuperBuilder // Позволяет использовать паттерн "строитель" для создания объектов
@AllArgsConstructor // Lombok создает конструктор, который принимает все поля как параметры
@NoArgsConstructor // Lombok создает конструктор по умолчанию (без параметров)
@Entity // Указывает, что класс является сущностью JPA, что позволяет ему отображаться в базе данных
@FieldDefaults(level = AccessLevel.PRIVATE) // Устанавливает уровень доступа к полям как "private"
public class Feedback extends BaseEntity { // Наследует от BaseEntity, что, вероятно, предоставляет уникальный идентификатор и временные метки

    // Поле для хранения оценки, вероятно, значений типа "рейтинг"
    Double note; // Оценка или рейтинг, который пользователь дал книге

    // Поле для хранения комментария или отзыва
    String comment; // Комментарий или текстовый отзыв, связанный с оценкой

    // Ассоциация Many-to-One с сущностью Book, указывающая, к какой книге относится отзыв
    @ManyToOne // Указывает на связь "многие к одному"
    @JoinColumn(name = "book_id") // Указывает, что внешний ключ хранится в этом столбце
            Book book; // Книга, о которой оставлен отзыв
}