/**
 Комментарии в коде:

 `@Service` - аннотация, указывающая, что данный класс является сервисом, который будет управляться контейнером Spring.
 `@RequiredArgsConstructor` - аннотация, которая автоматически создает конструктор с параметрами, соответствующими полям класса.
 `UserDetailsService` - интерфейс, который предоставляет методы для загрузки пользовательских данных из источника данных (например, базы данных) для аутентификации.
 `UserRepository` - репозиторий, который предоставляет методы для работы с данными пользователей в базе данных.
 `UserDetails` - интерфейс, который представляет детали пользователя, необходимые для аутентификации.
 `UsernameNotFoundException` - исключение, которое выбрасывается, если пользователь не найден по указанному имени пользователя (email).
 Метод `loadUserByUsername(String userEmail)` реализует метод `UserDetailsService`, который загружает детали пользователя по email. Метод использует `UserRepository` для поиска пользователя по email и возвращает объект `UserDetails`. Если пользователь не найден, выбрасывается исключение `UsernameNotFoundException`. Аннотация `@Transactional` указывает, что метод должен быть выполнен в рамках транзакции.
 **/
package com.yaroslav.booknetwork.security;

import com.yaroslav.booknetwork.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    /**
     * Поиск пользователя по email и загрузка его деталей для аутентификации.
     * Если пользователь не найден, выбрасывается исключение UsernameNotFoundException.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}