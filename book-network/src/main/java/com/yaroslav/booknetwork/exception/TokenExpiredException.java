package com.yaroslav.booknetwork.exception;

public class TokenExpiredException extends RuntimeException{
    // Конструктор с сообщением
    public TokenExpiredException(String message) {
        super(message); // Передает сообщение в конструктор RuntimeException
    }

    // Конструктор с сообщением и причиной (вложенное исключение)
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause); // Позволяет передавать причины исключения
    }

    // Конструктор по умолчанию
    public TokenExpiredException() {
        super("The token has expired."); // Сообщение по умолчанию для истекшего токена
    }
}
