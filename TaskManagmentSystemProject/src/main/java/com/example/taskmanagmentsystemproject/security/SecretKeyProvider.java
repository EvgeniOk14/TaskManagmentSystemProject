package com.example.taskmanagmentsystemproject.security;

import com.example.taskmanagmentsystemproject.exceptions.customExeptions.TokenNotSaveException;
import com.example.taskmanagmentsystemproject.models.token.JwtToken;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import java.util.Base64;
import io.jsonwebtoken.security.Keys;

/**
 * Класс SecretKeyProvider:
 *
 * Управляет секретным ключом для генерации и проверки JWT токенов.
 *
 * Основные функции:
 * - Загрузка существующего секретного ключа из базы данных MongoDB.
 * - Генерация нового секретного ключа, если ключ отсутствует.
 * - Предоставление текущего секретного ключа для использования в других компонентах.
 *
 * Зависимости:
 * - {@link MongoTemplate}: для взаимодействия с базой данных MongoDB.
 *
 * Методы:
 * - {@link #loadOrGenerateSecretKey()}: Загружает или генерирует секретный ключ.
 * - {@link #getSecretKey()}: Возвращает текущий секретный ключ.
 * - {@link #setSecretKey(String)}: Устанавливает новый секретный ключ.
 *
 * Исключения:
 * - {@link TokenNotSaveException}: кастомное исключение, обрабатываемое на глобальном уровне, в случае, если токен не может быть сохранён в базу данных MongoDB.
 *
 * Аннотации:
 * - @Component: указывает, что класс является Spring-компонентом, доступным для внедрения.
 */
@Component
public class SecretKeyProvider
{
    //region Fields
    private static final String SECRET_KEY_ID = "jwt-secret-key-id"; // Уникальный ID для хранения ключа в базе данных
    private String secretKey; // Секретный ключ для JWT
    private final MongoTemplate mongoTemplate;  // Для работы с MongoDB
    //endRegion

    //region Constructor
    /**
     * Конструктор класса SecretKeyProvider.
     * Выполняет инициализацию MongoTemplate и загружает или генерирует секретный ключ.
     *
     * @param mongoTemplate объект MongoTemplate для взаимодействия с базой данных.
     */
    public SecretKeyProvider(MongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;  // Инициализация mongoTemplate
        this.secretKey = loadOrGenerateSecretKey();  // Загрузка или генерация секретного ключа
    }
    //endRegion

    //region Methods
    /**
     * Метод loadOrGenerateSecretKey:
     * Загружает секретный ключ из базы данных или генерирует новый, если ключ отсутствует.
     *
     * @return строка, представляющая секретный ключ в формате Base64.
     * @exception TokenNotSaveException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если токен не может быть сохранён в базу данных
     */
    private String loadOrGenerateSecretKey()
    {
        try
        {
            // Проверяем, существует ли уже секретный ключ в базе данных
            JwtToken jwtToken = mongoTemplate.findOne(
                    Query.query(Criteria.where("id").is(SECRET_KEY_ID)),  // Ищем документ с конкретным ID
                    JwtToken.class);  // Тип документа - JwtToken

            if (jwtToken != null) // Если ключ существует в базе данных, то:
            {
                return jwtToken.getSecretKey();  // Возвращаем существующий секретный ключ
            }
            else // Если ключ не найден, генерируем новый секретный ключ, то:
            {
                byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();  // Генерация нового ключа
                String newSecretKey = Base64.getEncoder().encodeToString(keyBytes);  // Преобразуем ключ в строку Base64

                JwtToken newJwtToken = new JwtToken(); // Создаем новый объект JwtToken
                newJwtToken.setId(SECRET_KEY_ID);  // Устанавливаем уникальный ID для документа
                newJwtToken.setSecretKey(newSecretKey);  // Устанавливаем сгенерированный ключ
                mongoTemplate.save(newJwtToken);  // Сохраняем новый ключ в базе данных

                return newSecretKey;  // Возвращаем новый секретный ключ
            }
        }
        catch (TokenNotSaveException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если токен не может быть сохранён в базу данных
        {
            throw ex;
        }
    }

    /**
     * Метод getSecretKey:
     * Возвращает текущий секретный ключ для JWT.
     *
     * @return строка, представляющая секретный ключ.
     */
    public String getSecretKey() {
        return secretKey;  // Возвращаем текущий секретный ключ
    }

    /**
     * Метод setSecretKey:
     * Устанавливает новый секретный ключ.
     *
     * @param secretKey строка, представляющая новый секретный ключ.
     */
    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;  // Устанавливаем новый секретный ключ
    }
    //endRegion
}
