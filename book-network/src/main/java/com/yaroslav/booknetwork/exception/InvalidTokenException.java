package com.yaroslav.booknetwork.exception;

public class InvalidTokenException extends RuntimeException{
    // Конструктор с сообщением
    public InvalidTokenException(String message) {
        super(message); // Передает сообщение в конструктор RuntimeException
    }

    // Конструктор с сообщением и причиной (вложенное исключение)
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause); // Позволяет передавать причины исключения
    }
}
