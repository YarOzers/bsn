package com.yaroslav.booknetwork.auth;

import jakarta.validation.constraints.Email; // Импортируем аннотацию для проверки формата электронной почты
import jakarta.validation.constraints.NotBlank; // Импортируем аннотацию для проверки, что строка не пустая и содержит хотя бы один непробельный символ
import jakarta.validation.constraints.NotEmpty; // Импортируем аннотацию для проверки, что строка не пустая
import jakarta.validation.constraints.Size; // Импортируем аннотацию для проверки размера строки
import lombok.Builder; // Импортируем класс Builder для генерации конструктора с параметрами
import lombok.Getter; // Импортируем аннотацию для автоматической генерации геттеров
import lombok.Setter; // Импортируем аннотацию для автоматической генерации сеттеров

@Getter
@Setter
@Builder // Генерируем конструктор с параметрами с помощью Builder
public class RegistrationRequest {

    @NotEmpty(message = "Firstname is mandatory") // Аннотация для проверки, что поле firstName не пустое
    @NotBlank(message = "Firstname is mandatory") // Аннотация для проверки, что поле firstName не пустое и содержит хотя бы один непробельный символ
    private String firstname;

    @NotEmpty(message = "Lastname is mandatory") // Аннотация для проверки, что поле lastName не пустое
    @NotBlank(message = "Lastname is mandatory") // Аннотация для проверки, что поле lastName не пустое и содержит хотя бы один непробельный символ
    private String lastname;

    @Email(message = "Email is not formatted") // Аннотация для проверки, что поле email имеет правильный формат электронной почты
    @NotEmpty(message = "Email is mandatory") // Аннотация для проверки, что поле email не пустое
    @NotBlank(message = "Email is mandatory") // Аннотация для проверки, что поле email не пустое и содержит хотя бы один непробельный символ
    private String email;

    @NotEmpty(message = "Password is mandatory") // Аннотация для проверки, что поле password не пустое
    @NotBlank(message = "Password is mandatory") // Аннотация для проверки, что поле password не пустое и содержит хотя бы один непробельный символ
    @Size(min = 8, message = "Password should be 8 characters minimum") // Аннотация для проверки, что длина поля password не менее 8 символов
    private String password;
}
