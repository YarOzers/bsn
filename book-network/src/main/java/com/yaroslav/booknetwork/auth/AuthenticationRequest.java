package com.yaroslav.booknetwork.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {


    @Email(message = "Email is not formatted") // Аннотация для проверки, что поле email имеет правильный формат электронной почты
    @NotEmpty(message = "Email is mandatory") // Аннотация для проверки, что поле email не пустое
    @NotBlank(message = "Email is mandatory") // Аннотация для проверки, что поле email не пустое и содержит хотя бы один непробельный символ
    private String email;

    @NotEmpty(message = "Password is mandatory") // Аннотация для проверки, что поле password не пустое
    @NotBlank(message = "Password is mandatory") // Аннотация для проверки, что поле password не пустое и содержит хотя бы один непробельный символ
    @Size(min = 8, message = "Password should be 8 characters minimum") // Аннотация для проверки, что длина поля password не менее 8 символов
    private String password;
}
