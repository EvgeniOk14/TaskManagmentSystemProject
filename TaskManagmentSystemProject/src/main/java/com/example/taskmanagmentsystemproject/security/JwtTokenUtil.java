package com.example.taskmanagmentsystemproject.security;

import com.example.taskmanagmentsystemproject.models.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Function;

/**
 * Утилитный класс JwtTokenUtil:
 *
 * Предназначен для работы с JWT токенами.
 * Основные функции:
 * - Генерация JWT токенов.
 * - Извлечение данных (клеймов) из токенов.
 * - Проверка валидности токенов.
 * - Получение информации о дате истечения срока действия токенов.
 *
 * Токены подписываются с использованием алгоритма HMAC SHA-512 и секретного ключа, который предоставляется через {@link SecretKeyProvider}.
 *
 * Ключевые методы:
 * - {@link #generateToken(User)}: Создание токена для пользователя.
 * - {@link #extractUsername(String)}: Извлечение имени пользователя из токена.
 * - {@link #isTokenValid(String, UserDetails)}: Проверка, валиден ли токен.
 * - {@link #isTokenExpired(String)}: Проверка, истек ли срок действия токена.
 * - {@link #getExpirationDate(String)}: Извлечение даты истечения токена.
 */
@Component
public class JwtTokenUtil
{
    //region Fields
    private final byte[] decodedSecretKey; // Храним массив байтов для декодированного секретного ключа
    private final long JWT_EXPIRATION = 1000 * 60 * 60 * 10; // Время истечения действия токена (10 часов)
    //endRegion

    //region Constructor
    /**
     * Конструктор с внедрением зависимостей.
     * Этот конструктор получает секретный ключ через SecretKeyProvider,
     * декодирует его из Base64 строки и сохраняет как массив байтов.
     * @param secretKeyProvider Провайдер для получения секретного ключа
     */
    public JwtTokenUtil(SecretKeyProvider secretKeyProvider)
    {
        this.decodedSecretKey = Base64.getDecoder().decode(secretKeyProvider.getSecretKey()); // Декодируем ключ из Base64 в массив байтов

        System.out.println("Key length: " + decodedSecretKey.length * 8 + " bits"); // Выводим длину ключа для отладки
    }
    //endRegion

    //region Methods
    /**
     * Генерация нового JWT токена для пользователя.
     * Этот метод создает JWT токен, который включает в себя:
     *  - subject (имя пользователя, email),
     *  - дату создания токена,
     *  - время истечения токена (через 10 часов).
     * @param user Пользователь, для которого генерируется токен
     * @return JWT токен, представляющий пользователя
     */
    public String generateToken(User user)
    {
        return Jwts.builder()  // Создаем новый JWT токен
                .setSubject(user.getEmail()) // Субъект токена - email пользователя
                .setIssuedAt(new Date()) // Устанавливаем время выпуска токена
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Устанавливаем время истечения (10 часов)
                .signWith(SignatureAlgorithm.HS512, decodedSecretKey) // Подписываем токен с помощью секретного ключа
                .compact(); // Завершаем сборку и возвращаем токен как строку
    }

    /**
     * Извлекает имя пользователя (subject) из JWT токена.
     * Этот метод используется для извлечения поля "subject",
     * которое обычно содержит идентификатор или email пользователя.
     * @param token JWT токен
     * @return Имя пользователя (email), извлеченное из токена
     */
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject); // Извлекаем имя пользователя из токена
    }

    /**
     * Извлекает конкретное клейм (claim) из токена.
     * Этот метод позволяет извлекать различные клеймы, например, дату истечения или другие данные.
     * @param token JWT токен
     * @param claimsResolver Функция для извлечения нужного клейма
     * @param <T> Тип возвращаемого значения
     * @return Значение клейма, которое соответствует запросу
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token); // Извлекаем все клеймы из токена
        return claimsResolver.apply(claims); // Применяем функцию для извлечения конкретного клейма
    }

    /**
     * Извлекает все клеймы из JWT токена.
     * Этот метод декодирует токен и извлекает все утверждения (claims), которые он содержит.
     * @param token JWT токен
     * @return Все клеймы, содержащиеся в токене
     */
    private Claims extractAllClaims(String token)
    {
        return Jwts.parser()  // Создаем парсер для токена
                .setSigningKey(decodedSecretKey) // Устанавливаем секретный ключ для верификации подписи
                .build()
                .parseClaimsJws(token) // Парсим JWT токен
                .getBody(); // Извлекаем тело токена (клеймы)
    }

    /**
     * Проверяет, действителен ли токен для указанного пользователя.
     * Этот метод проверяет, соответствует ли имя пользователя в токене имени пользователя в объектах UserDetails,
     * а также не истек ли токен.
     * @param token JWT токен
     * @return true, если токен валиден (имя пользователя совпадает и токен не истек), иначе false
     */
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token); // Извлекаем имя пользователя из токена
        // Получаем аутентифицированного пользователя из SecurityContext
        //UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // userDetails Детали пользователя для проверки
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); // Проверяем совпадение и срок действия токена
    }

    /**
     * Проверяет, истек ли срок действия токена.
     * Этот метод проверяет дату истечения токена и сравнивает её с текущей датой.
     * @param token JWT токен
     * @return true, если срок действия токена истек, иначе false
     */
    public boolean isTokenExpired(String token)
    {
        Date expiration = extractClaim(token, Claims::getExpiration); // Извлекаем дату истечения токена
        return expiration.before(new Date()); // Проверяем, истек ли срок действия токена
    }

    /**
     * Получает дату истечения срока действия токена.
     * Этот метод извлекает клейм "expiration" из токена, который указывает на дату истечения.
     * @param token JWT токен
     * @return Дата истечения токена
     */
    public Date getExpirationDate(String token)
    {
        return extractClaim(token, Claims::getExpiration); // Извлекаем дату истечения из клейма
    }

    /**
     * Извлекает роль пользователя из JWT токена.
     * Этот метод извлекает клейм "role", который может содержать роль пользователя (например, "ADMIN", "USER").
     * @param token JWT токен
     * @return Роль пользователя, извлеченная из токена
     */
    public String extractRoleFromToken(String token)
    {
        return extractClaim(token, claims -> claims.get("role", String.class)); // Извлекаем роль из токена
    }
    //endRegion
}



