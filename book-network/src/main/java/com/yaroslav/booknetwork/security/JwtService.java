/**
 * `        extractUserName(String token)`: Этот метод извлекает имя пользователя из переданного токена. Он вызывает вспомогательный метод `extractClaim`, передавая ему функцию, которая извлекает имя пользователя из объекта `Claims`.
 * `extractClaim(String token, Function<Claims, T> claimResolver)`: Этот метод извлекает значение конкретного утверждения из переданного токена. Он вызывает вспомогательный метод `extractAllClaims`, чтобы получить все утверждения из токена, а затем применяет переданную функцию `claimResolver`, чтобы извлечь значение конкретного утверждения.
 * `extractAllClaims(String token)`: Этот метод извлекает все утверждения из переданного токена. Он использует библиотеку `io.jsonwebtoken` для разбора токена и получения объекта `Claims`, содержащего все утверждения.
 * `generateToken(UserDetails userDetails)`: Этот метод генерирует новый токен для переданного пользователя. Он вызывает вспомогательный метод `generateToken`, передавая ему пустой словарь дополнительных утверждений и объект `UserDetails`.
 * `generateToken(Map<String, Object> claims, UserDetails userDetails)`: Этот метод генерирует новый токен для переданного пользователя с дополнительными утверждениями. Он вызывает вспомогательный метод `buildToken`, передавая ему дополнительные утверждения, объект `UserDetails` и время жизни токена.
 * `buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long jwtExpiration)`: Этот метод строит новый токен с дополнительными утверждениями, именем пользователя, временем выдачи, временем истечения срока действия и списком ролей пользователя. Он использует библиотеку `io.jsonwebtoken` для создания токена с переданными параметрами и подписывает его секретным ключом.
 * `isTokenValid(String token, UserDetails userDetails)`: Этот метод проверяет, является ли переданный токен действительным для переданного пользователя. Он извлекает имя пользователя из токена и сравнивает его с именем пользователя из объекта `UserDetails`. Затем он проверяет, не истек ли срок действия токена с помощью метода `isTokenExpired`.
 * `isTokenExpired(String token)`: Этот метод проверяет, истек ли срок действия переданного токена. Он извлекает время истечения срока действия из токена и сравнивает его с текущей датой.
 * `extractExpiration(String token)`: Этот метод извлекает время истечения срока действия из переданного токена. Он вызывает вспомогательный метод `extractClaim`, передавая ему функцию, которая извлекает время истечения срока действия из объекта `Claims`.
 * `getSignInKey()`: Этот метод возвращает секретный ключ для подписи токена. Он декодирует секретный ключ из строки в байты с помощью `Decoders.BASE64`, а затем создает ключ HMAC-SHA256 с помощью `Keys.hmacShaKeyFor`.
 * <p>
 * Этот код представляет собой реализацию сервиса для работы с JSON Web Tokens (JWT). Вот комментарии к каждому методу:
 * <p>
 * `extractUserName(String token)` - извлекает имя пользователя из токена JWT.
 * `extractClaim(String token, Function<Claims, T> claimResolver)` - извлекает конкретное утверждение из токена JWT.
 * `extractAllClaims(String token)` - извлекает все утверждения из токена JWT.
 * `generateToken(UserDetails userDetails)` - генерирует новый токен JWT для указанного пользователя.
 * `generateToken(Map<String, Object> claims, UserDetails userDetails)` - генерирует новый токен JWT для указанного пользователя с дополнительными утверждениями.
 * `buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long jwtExpiration)` - строит новый токен JWT с указанными утверждениями, именем пользователя и временем истечения срока действия.
 * `isTokenValid(String token, UserDetails userDetails)` - проверяет, является ли токен JWT действительным для указанного пользователя.
 * `isTokenExpired(String token)` - проверяет, истек ли срок действия токена JWT.
 * `extractExpiration(String token)` - извлекает время истечения срока действия токена JWT.
 * `getSignInKey()` - получает ключ для подписи токена JWT, используя заданный секретный ключ.
 * Также в классе есть две переменные класса `jwtExpiration` и `secretKey`, которые задают время жизни токена JWT и секретный ключ для его подписи соответственно. Они получаются из настроек приложения с помощью аннотации `@Value`.
 **/
package com.yaroslav.booknetwork.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//@Service
@Data
@Slf4j
public class JwtService {
//
//    @Value("${application.security.jwt.expiration}")
//    private long jwtExpiration;
//
//    @Value("${application.security.jwt.secret-key}")
//    private String secretKey;
//
//    //    Этот метод извлекает имя пользователя из переданного токена. Он вызывает вспомогательный метод `extractClaim`, передавая ему функцию, которая извлекает имя пользователя из объекта `Claims`.
//    public String extractUserName(String token) {
//        log.info("Extracting username from token");
//        String username = extractClaim(token, Claims::getSubject);
//        log.debug("Extracted username: {}", username);
//        return username;
//    }
//
//    //    Этот метод извлекает значение конкретного утверждения из переданного токена. Он вызывает вспомогательный метод `extractAllClaims`, чтобы получить все утверждения из токена, а затем применяет переданную функцию `claimResolver`, чтобы извлечь значение конкретного утверждения.
//    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
//        log.info("Extracting claim from token");
//        final Claims claims = extractAllClaims(token);
//        T result = claimResolver.apply(claims);
//        log.debug("Extracted claim: {}", result);
//        return result;
//    }
//
//    //    Этот метод извлекает все утверждения из переданного токена. Он использует библиотеку `io.jsonwebtoken` для разбора токена и получения объекта `Claims`, содержащего все утверждения.
//    private Claims extractAllClaims(String token) {
//        log.info("Extracting all claims from token");
//        Claims claims = Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        log.debug("Extracted claims: {}", claims);
//        return claims;
//    }
//
//    //    Этот метод генерирует новый токен для переданного пользователя. Он вызывает вспомогательный метод `generateToken`, передавая ему пустой словарь дополнительных утверждений и объект `UserDetails`.
//    public String generateToken(UserDetails userDetails) {
//        log.info("Generating token for user: {}", userDetails.getUsername());
//        String token = generateToken(new HashMap<>(), userDetails);
//        log.debug("Generated token: {}", token);
//        return token;
//    }
//
//    //    Этот метод генерирует новый токен для переданного пользователя с дополнительными утверждениями. Он вызывает вспомогательный метод `buildToken`, передавая ему дополнительные утверждения, объект `UserDetails` и время жизни токена.
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        log.info("Generating token with extra claims for user: {}", userDetails.getUsername());
//        String token = buildToken(extraClaims, userDetails, jwtExpiration);
//        log.debug("Generated token with extra claims: {}", token);
//        return token;
//    }
//
//    //    Этот метод строит новый токен с дополнительными утверждениями, именем пользователя, временем выдачи, временем истечения срока действия и списком ролей пользователя. Он использует библиотеку `io.jsonwebtoken` для создания токена с переданными параметрами и подписывает его секретным ключом.
//    private String buildToken(
//            Map<String, Object> extraClaims, // Дополнительные претензии (требования) для JWT-токена
//            UserDetails userDetails, // Данные пользователя
//            long jwtExpiration // Время жизни JWT-токена в миллисекундах
//    ) {
//        log.info("Building token for user: {}", userDetails.getUsername());
//        // Получаем список ролей/прав пользователя из UserDetails
//        var authorities = userDetails.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();
//
//        // Строим JWT-токен с использованием JJWT библиотеки
//        System.out.println();
//        String token = Jwts
//                .builder()
//                // Устанавливаем дополнительные претензии (требования) для JWT-токена
//                .setClaims(extraClaims)
//                // Устанавливаем имя пользователя в качестве субъекта JWT-токена
//                .setSubject(userDetails.getUsername())
//                // Устанавливаем время выдачи JWT-токена
//                .setIssuedAt(new Date(System.currentTimeMillis()))
////                // Устанавливаем время истечения срока действия JWT-токена
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                // Добавляем список ролей/прав пользователя в JWT-токен
//                .claim("authorities", authorities)
//                // Подписываем JWT-токен с использованием секретного ключа
//                .signWith(getSignInKey())
//                // Сжимаем и возвращаем JWT-токен в виде строки
//                .compact();
//        log.debug("Built token: {}", token);
//        return token;
//
//    }
//
//    //    Этот метод проверяет, является ли переданный токен действительным для переданного пользователя. Он извлекает имя пользователя из токена и сравнивает его с именем пользователя из объекта `UserDetails`. Затем он проверяет, не истек ли срок действия токена с помощью метода `isTokenExpired`.
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        log.info("Validating token for user: {}", userDetails.getUsername());
//        final String username = extractUserName(token);
//        boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//        log.debug("Token is valid: {}", isValid);
//        return isValid;
//    }
//
//    //    Этот метод проверяет, истек ли срок действия переданного токена. Он извлекает время истечения срока действия из токена и сравнивает его с текущей датой.
//    private boolean isTokenExpired(String token) {
//        log.info("Checking if token has expired");
//        Date expiration = extractExpiration(token);
//        boolean expired = expiration.before(new Date());
//        log.debug("Token expired: {}", expired);
//        return expired;
//    }
//
//    //    Этот метод извлекает время истечения срока действия из переданного токена. Он вызывает вспомогательный метод `extractClaim`, передавая ему функцию, которая извлекает время истечения срока действия из объекта `Claims`.
//    private Date extractExpiration(String token) {
//        log.info("Extracting token expiration");
//        Date expiration = extractClaim(token, Claims::getExpiration);
//        log.debug("Extracted expiration: {}", expiration);
//        return expiration;
//    }
//
//    //    Этот метод возвращает секретный ключ для подписи токена. Он декодирует секретный ключ из строки в байты с помощью `Decoders.BASE64`, а затем создает ключ HMAC-SHA256 с помощью `Keys.hmacShaKeyFor`.
//    private Key getSignInKey() {
//        log.info("Getting sign-in key");
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        Key signInKey = Keys.hmacShaKeyFor(keyBytes);
//        log.debug("Retrieved sign-in key");
//        return signInKey;
//    }
}

