package com.yaroslav.booknetwork.book;

// Импорт необходимых классов из других пакетов

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yaroslav.booknetwork.common.BaseEntity; // Общий базовый класс для сущностей, содержащий общие поля (например, ID, timestamp и т. д.)
import com.yaroslav.booknetwork.feedback.Feedback; // Класс Feedback, представляющий отзывы о книге
import com.yaroslav.booknetwork.history.BookTransactionHistory; // Класс BookTransactionHistory, представляющий историю транзакций книги
import com.yaroslav.booknetwork.user.User; // Класс User, представляющий владельца книги
import jakarta.persistence.*; // Импорт аннотаций JPA для ORM (Object-Relational Mapping)
import lombok.*; // Импорт аннотаций Lombok для сокращения написания кода
import lombok.experimental.FieldDefaults; // Установка уровня доступа к полям с помощью аннотации
import lombok.experimental.SuperBuilder; // Позволяет создавать объекты с использованием шаблона "строитель"

import java.util.List; // Импорт списка, чтобы иметь возможность хранить коллекции объектов

/**
 * Сущность Book представляет книгу в базе данных.
 * Наследуется от BaseEntity, что означает, что эта сущность будет иметь базовые свойства,
 * такие как идентификатор, временные метки и т. д.
 */
@Getter // Lombok создает методы "геттеры" для всех полей
@Setter // Lombok создает методы "сеттеры" для всех полей
@SuperBuilder // Позволяет создавать объекты с использованием шаблона "строитель" с возможностью наследования
@AllArgsConstructor // Lombok создает конструктор, принимающий все параметры
@NoArgsConstructor // Lombok создает конструктор по умолчанию (без параметров)
@Entity // Аннотация JPA, которая указывает, что этот класс является сущностью базы данных
@FieldDefaults(level = AccessLevel.PRIVATE) // Устанавливает уровень доступа к полям как "private"
public class Book extends BaseEntity { // Наследуется от BaseEntity

    // Основные свойства книги
    String title; // Название книги
    String authorName; // Имя автора
    String isbn; // Международный стандартный книжный номер
    @Column(columnDefinition = "TEXT")
    String synopsis; // Краткое описание или аннотация книги
    @Column(columnDefinition = "TEXT")
    String bookCover; // Обложка книги (может быть ссылка на изображение)
    boolean archived; // Флаг, указывающий, что книга архивирована
    boolean shareable; // Флаг, указывающий, что книгу можно делить или передавать

    // Ассоциация Many-to-One: книга принадлежит одному владельцу (User)
//    @ManyToOne // Определяет связь "многие к одному"
//    @JoinColumn(name = "owner_id") // Указывает, что внешний ключ хранится в этой таблице
//    @JsonIgnore
//    User owner; // Владелец книги (связь с User)

    // Ассоциация One-to-Many: книга может иметь несколько отзывов (Feedback)
    @OneToMany(mappedBy = "book") // Определяет связь "один ко многим", связанная через поле "book"
            List<Feedback> feedbacks; // Список отзывов о книге

    // Ассоциация One-to-Many: книга может иметь несколько историй транзакций (BookTransactionHistory)
    @OneToMany(mappedBy = "book") // Определяет связь "один ко многим", связанная через поле "book"
            List<BookTransactionHistory> histories; // Список историй транзакций для этой книги

    @Transient // Указывает, что это вычисляемое значение не сохраняется в базу данных
    public double getRate() {
        // Проверяет, есть ли отзывы о книге. Если нет отзывов или список пуст, возвращает 0.0
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0; // Возвращает 0.0, если нет отзывов
        }

        // Рассчитывает среднюю оценку по всем отзывам, используя стримы Java
        var rate = this.feedbacks.stream() // Создает поток данных из списка отзывов
                .mapToDouble(Feedback::getNote) // Преобразует каждую оценку отзыва в поток double
                .average() // Рассчитывает среднюю оценку
                .orElse(0.0); // Если отзывов нет, возвращает 0.0

        // Округляет результат до одного знака после запятой
        double roundedRate = Math.round(rate * 10.0) / 10.0; // Округляет среднюю оценку до одного знака после запятой

        // Возвращает округленный средний рейтинг
        return roundedRate; // Возвращает среднюю оценку книги
    }
}
