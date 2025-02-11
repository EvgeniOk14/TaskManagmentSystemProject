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
    // 1000 — это количество миллисекунд в одной секунде.
    // 60 — это количество секунд в одной минуте.
    // 60 — это количество минут в одном часу.
    // 10 — это количество часов, на которые устанавливаем срок действия токена.
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
        this.decodedSecretKey = Base64.getDecoder().decode(secretKeyProvider.getSecretKeyFromSecretKeyProvider()); // Декодируем ключ из строки Base64 в массив байтов (декодирует строку Base64 в массив байтов)

        System.out.println("Длина декодированного ключа: " + decodedSecretKey.length * 8 + "  в битах"); // Выводим длину ключа для отладки
        // Преобразование байтов в биты: В одном байте содержится 8 бит, позволяет получить длину ключа в битах
    }
    //endRegion

    //region Methods
    /**
     * Генерация нового JWT токена для пользователя.
     * Этот метод создает JWT токен, который включает в себя:
     *  - subject (имя пользователя, email),
     *  - дату создания токена,
     *  - время истечения токена (через 10 часов).
     *
     *  - Jwts — это класс, предоставляемый библиотекой jjwt (Java JWT),
     *    которая используется для создания и обработки JSON Web Tokens (JWT) в Java-приложениях.
     *    Эта библиотека позволяет легко генерировать, парсить и валидировать JWT токены
     *    Создание токена (Jwts.builder(); Парсинг токена метод parser() и Валидация токена)
     *
     *  - Метод builder() возвращает объект JwtBuilder, который используется для создания нового JWT токена
     *    Он инициализирует процесс создания токена, позволяя устанавливать различные его параметры (субъект, время выпуска, время истечения, подпись и т.д.).
     *
     *  - setSubject(...): Метод, который устанавливает "субъект" токена.
     *    Субъект обычно представляет собой идентификатор пользователя или информацию, связанную с пользователем.
     *    В данном случае это email пользователя, получаемый через метод getEmail() объекта User.
     *
     *  - setIssuedAt(...): Метод, который устанавливает время создания токена.
     *    Здесь используется new Date(), что создает объект Date, представляющий текущее время.
     *    Это время будет записано в токен, чтобы обозначить, когда он был выдан.
     *
     *  - setExpiration(...): Метод, который устанавливает время истечения действия токена.
     *    new Date(System.currentTimeMillis() + JWT_EXPIRATION) создает новый объект Date,
     *    который представляет собой текущее время, увеличенное на JWT_EXPIRATION (в данном случае 10 часов в миллисекундах).
     *    Это означает, что токен будет действителен в течение 10 часов с момента его создания
     *
     *  - signWith(...): Метод, который используется для подписи токена, где:
     *    1) SignatureAlgorithm.HS512: Алгоритм подписи ( HS512 — это HMAC с использованием SHA-512, который обеспечивает высокий уровень безопасности)
     *    2) decodedSecretKey: Секретный ключ, используемый для подписи токена
     *
     *  - compact(): Метод, который завершает процесс построения токена и возвращает его в виде строки.
     *    Эта строка представляет собой сам JWT токен, который можно отправить клиенту или использовать в дальнейшем
     *    для аутентификации
     *
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
     * Метод extractUsername
     * Извлекает конкретный клейм, в данном случае имя пользователя (subject) из JWT токена.
     * Этот метод используется для извлечения поля "subject",
     * которое обычно содержит идентификатор или email пользователя.
     *
     * - Claims: представляет собой набор утверждений (данных), содержащихся в токене
     *
     * - String token: Это JWT токен, из которого необходимо извлечь информацию, например имя пользователя.
     *
     * - Claims::getSubject — это синтаксис ссылки на метод (method reference) в Java,
     *   который позволяет ссылаться на существующий метод без необходимости писать лямбда-выражение
     *
     * - Claims — это класс, а getSubject — это метод этого класса, который возвращает значение поля "subject"
     *   (обычно это идентификатор пользователя или email)
     *
     * @param token JWT токен
     *
     * @return Возвращает имя пользователя (email), извлеченное из токена
     */
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject); // Извлекаем имя пользователя из токена
    }

    /**
     * Извлекает конкретное клейм (claim) из токена в данном случае имя пользователя (subject) из JWT токена
     * Этот метод позволяет извлекать различные клеймы, например, дату истечения или другие данные.
     *
     * - Метод apply - это единственный абстрактный метод в интерфейсе Function,
     *   который является частью стандартной библиотеки Java,
     *   вызывается на объекте claimsResolver и передает ему объект claims,
     *   который был извлечен из токена с помощью метода extractAllClaims.
     *   R apply(T t); где: R: значение, возвращаемое функцией, T t: входной параметр, который передается в функцию
     *
     * - Function<Claims, T> является частью пакета java.util.function, принимает один аргумент типа Claims и возвращает другой типа T
     *
     * @param token JWT токен
     * @param claimsResolver Функция для извлечения нужного клейма (функция, которая принимает объект Claims и тип возвращаемого значения T и возвращает значение нужного клейма)
     * @param <T> Тип возвращаемого значения
     * @return Значение клейма, которое соответствует запросу
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token); // Извлекаем все клеймы из токена
        return claimsResolver.apply(claims); // Применяем функцию для извлечения конкретного клейма
                                            // (в функцию claimsResolver было передано значение Claims::getSubject,
                                           // которое получает имя пользователя subject, т.е. в данном случае его email)
                                          // т.е. вернёт mail пользователя
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
     *
     * - UserDetails — это интерфейс, который определяет основные методы для работы с информацией о пользователе.
     *   Он находится в пакете org.springframework.security.core.userdetails.
     *   Этот интерфейс предоставляет методы для получения данных о пользователе,
     *   таких как имя пользователя, пароль, роли, и другие учетные данные
     *   Методы интерфейса UserDetails:
     *     - String getUsername(): возвращает имя пользователя.
     *     - String getPassword(): возвращает пароль (обычно хранится в зашифрованном виде).
     *     - Collection<? extends GrantedAuthority> getAuthorities(): возвращает роли или разрешения пользователя.
     *     - boolean isAccountNonExpired(): проверяет, не истек ли срок действия учетной записи.
     *     - boolean isAccountNonLocked(): проверяет, не заблокирована ли учетная запись.
     *     - boolean isCredentialsNonExpired(): проверяет, не истек ли срок действия учетных данных.
     *     - boolean isEnabled(): проверяет, активна ли учетная запись.
     */
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token); // Извлекаем имя пользователя из токена
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



