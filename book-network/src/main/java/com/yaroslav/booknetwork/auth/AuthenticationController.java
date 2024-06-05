package com.yaroslav.booknetwork.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication") // Добавляем тег для Swagger документации
@Slf4j
public class AuthenticationController {

//    private final AuthenticationService service; // Инъектируем сервис для работы с аутентификацией
//
//    @PostMapping("/register")
//    // Создаем эндпоинт для регистрации пользователя
//    @ResponseStatus(HttpStatus.ACCEPTED) // Устанавливаем статус ответа на ACCEPTED (202)
//    public ResponseEntity<?> register(
//            @RequestBody @Valid RegistrationRequest request // Принимаем запрос на регистрацию с валидацией данных
//    ) throws MessagingException {
//        service.register(request); // Вызываем метод сервиса для регистрации пользователя
//        return ResponseEntity
//                .accepted()
//                .build(); // Возвращаем пустой ответ с кодом статуса ACCEPTED
//    }
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<AuthenticationResponse> authenticate(
//            @RequestBody @Valid AuthenticationRequest request
//    ){
//        return ResponseEntity.ok(service.authenticate(request));
//    }
//
//    @GetMapping("/activate-account")
//    public void confirm(
//            @RequestParam String token
//    ) throws MessagingException {
//        service.activateAccount(token);
//    }
}
