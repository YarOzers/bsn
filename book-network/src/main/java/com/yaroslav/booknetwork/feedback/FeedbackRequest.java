package com.yaroslav.booknetwork.feedback;

import jakarta.validation.constraints.*; // Импорт различных аннотаций для валидации полей

// Класс `FeedbackRequest` используется для передачи данных о запросе на создание или обновление отзыва.
public record FeedbackRequest(
        @Positive(message = "200") // Поле `note` должно быть положительным числом
        @Min(value = 0, message = "201") // Минимальное значение `note` должно быть 0
        @Max(value = 5, message = "202") // Максимальное значение `note` должно быть 5
        Double note, // Оценка отзыва, допускающая значения от 0 до 5

        @NotNull(message = "203") // Поле `comment` не должно быть `null`
        @NotEmpty(message = "203") // Поле `comment` не должно быть пустым
        @NotBlank(message = "203") // Поле `comment` не должно содержать только пробелы
        String comment, // Комментарий отзыва, который не может быть пустым

        @NotNull(message = "204") // Поле `bookId` не должно быть `null`
        Integer bookId // Идентификатор книги, для которой создается отзыв
) {
}

