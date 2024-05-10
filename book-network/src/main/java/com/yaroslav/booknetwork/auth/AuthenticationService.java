package com.yaroslav.booknetwork.auth;

import com.yaroslav.booknetwork.email.EmailService;
import com.yaroslav.booknetwork.email.EmailTemplateName;
import com.yaroslav.booknetwork.exception.InvalidTokenException;
import com.yaroslav.booknetwork.exception.TokenExpiredException;
import com.yaroslav.booknetwork.role.RoleRepository;
import com.yaroslav.booknetwork.security.JwtService;
import com.yaroslav.booknetwork.user.Token;
import com.yaroslav.booknetwork.user.TokenRepository;
import com.yaroslav.booknetwork.user.User;
import com.yaroslav.booknetwork.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final RoleRepository roleRepository; // Инъектируем репозиторий для работы с ролями
    private final PasswordEncoder passwordEncoder; // Инъектируем компонент для шифрования паролей
    private final UserRepository userRepository; // Инъектируем репозиторий для работы с пользователями
    private final TokenRepository tokenRepository; // Инъектируем репозиторий для работы с токенами
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException { // Метод для регистрации пользователя

        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized")); // Получаем роль пользователя

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Шифруем пароль пользователя
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build(); // Создаем объект пользователя

        userRepository.save(user); // Сохраняем пользователя в базе данных

        sendValidationEmail(user); // Отправляем письмо для подтверждения регистрации
    }

    private void sendValidationEmail(User user) throws MessagingException { // Метод для отправки письма для подтверждения регистрации
        var newToken = generateAndSaveActivationToken(user); // Генерируем и сохраняем токен для активации пользователя
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
        log.info("Токен активации отправлен на почту: {}", newToken);
    }

    private String generateAndSaveActivationToken(User user) { // Метод для генерации и сохранения токена для активации пользователя

        // Генерируем токен
        String generatedToken = generateActivationCode(6);

        var token = Token.builder()
                .token(generatedToken)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 15)))
                .user(user)
                .build(); // Создаем объект токена

        tokenRepository.save(token); // Сохраняем токен в базе данных

        return generatedToken; // Возвращаем сгенерированный токен
    }

    private String generateActivationCode(int length) { // Метод для генерации случайного кода активации

        String characters = "0123456789"; // Строка, содержащая возможные символы кода активации
        StringBuilder codeBuilder = new StringBuilder(); // Строка для построения кода активации
        SecureRandom secureRandom = new SecureRandom(); // Генератор случайных чисел

        for (int i = 0; i < length; i++) { // Генерируем каждый символ кода активации
            int randomIndex = secureRandom
                    .nextInt(characters.length()); // Выбираем случайный индекс символа из строки characters
            codeBuilder
                    .append(characters
                            .charAt(randomIndex)); // Добавляем выбранный символ в код активации
        }

        return codeBuilder.toString(); // Возвращаем сгенерированный код активации
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Аутентификация пользователя с использованием email и пароля из запроса
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // Создание карты утверждений (claims) с полным именем пользователя
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullName", user);

        // Генерация JWT токена с использованием карты утверждений и пользователя
        var jwtToken = jwtService.generateToken(claims, user);
        System.out.println(jwtToken);

        // Возвращение объекта AuthenticationResponse с токеном

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }


    //    @Transactional
    public void activateAccount(String token) throws MessagingException {
        // Поиск токена активации. Если не найден, выбросить собственное исключение
        Token activationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid activation token"));

        // Проверка срока действия токена. Если истек, отправить новый и выбросить исключение
        if (new Date().after(activationToken.getExpiresAt())) {
            sendValidationEmail(activationToken.getUser());
            throw new TokenExpiredException("Activation token has expired. A new token has been sent.");
        }

        // Поиск пользователя по ID, выброс исключения, если не найден
        User user = userRepository.findById(activationToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Активация учетной записи пользователя
        user.setEnabled(true);
        userRepository.save(user); // Сохраняем изменения

        // Обновление времени валидации токена
        activationToken.setValidatedAt(new Date());
        tokenRepository.save(activationToken); // Сохраняем токен
    }

}
