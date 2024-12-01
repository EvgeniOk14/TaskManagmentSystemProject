package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.exceptions.customExeptions.TokenNotDeleteException;
import com.example.taskmanagmentsystemproject.exceptions.customExeptions.TokenNotGenerateException;
import com.example.taskmanagmentsystemproject.exceptions.customExeptions.TokenNotSaveException;
import com.example.taskmanagmentsystemproject.exceptions.customExeptions.UserNotFoundException;
import com.example.taskmanagmentsystemproject.models.token.JwtToken;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.repository.JwtTokenRepository;
import com.example.taskmanagmentsystemproject.security.JwtTokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Сервис JwtTokenService:
 *
 * Предоставляет бизнес-логику для работы с JWT токенами.
 * Отвечает за генерацию, сохранение, обновление, получение и удаление токенов.
 *
 * Ключевые функции:
 * - Генерация JWT токенов для пользователей.
 * - Проверка срока действия токенов.
 * - Удаление устаревших токенов.
 * - Работа с базой данных через {@link JwtTokenRepository}.
 *
 * Зависимости:
 * - {@link JwtTokenRepository}: для доступа к данным токенов в базе.
 * - {@link JwtTokenUtil}: для работы с JWT токенами (генерация и проверка срока действия).
 *
 * Методы:
 * - {@link #generateAndSaveToken(User)}: генерирует новый токен для пользователя и сохраняет его в базе.
 * - {@link #getTokenByUserId(Integer)}: получает токен по идентификатору пользователя.
 * - {@link #refreshToken(User)}: обновляет токен для пользователя, если текущий истек.
 * - {@link #deleteExpiredTokens()}: удаляет все токены с истекшим сроком действия.
 *
 * Исключения:
 * - {@link IllegalArgumentException}: системное исключение выбрасывается, если переданный аргумент недействителен.
 * - {@link NullPointerException}:  системное исключение обрабатываемое на глобальном уровне, выбрасывается в случае обращения к отсутствующим данным.
 * - {@link TokenNotGenerateException}: кастоммное исключение обрабатываемое на глобальном уровне, выбрасывается в случае невозможности генерации токена
 * - {@link TokenNotSaveException}: кастоммное исключение обрабатываемое на глобальном уровне, выбрасывается в случае невозможности сохранения токена
 * - {@link TokenNotDeleteException}: кастоммное исключение обрабатываемое на глобальном уровне, выбрасывается в случае невозможности удаления токена
 *
 * Аннотации:
 * - {@link Service}: указывает, что класс является сервисным компонентом в Spring.
 * - {@link Transactional}: обеспечивает атомарность операций внутри методов.
 */
@Service
public class JwtTokenService
{
    //region Fields
    private final JwtTokenRepository jwtTokenRepository; // Репозиторий для работы с JWT токенами
    private final JwtTokenUtil jwtTokenUtil; // Утилита для работы с JWT токенами
    //endRegion

    //region Constructor
    /**
     * Конструктор JwtTokenService:
     * Инициализирует зависимости для работы с JWT токенами.
     *
     * @param jwtTokenRepository Репозиторий для работы с JWT токенами.
     * @param jwtTokenUtil Утилита для генерации и проверки токенов.
     */
    public JwtTokenService(JwtTokenRepository jwtTokenRepository, JwtTokenUtil jwtTokenUtil)
    {
        this.jwtTokenRepository = jwtTokenRepository; // Инициализация репозитория
        this.jwtTokenUtil = jwtTokenUtil; // Инициализация утилиты
    }
    //endRegion

    //region Methods
    /**
     * Метод generateAndSaveToken:
     * Генерирует новый JWT токен для пользователя и сохраняет его в базе данных.
     *
     * @param user Пользователь, для которого создается токен.
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception TokenNotSaveException - кастоммное исключение на глобальном уровне, в случае невозможности сохранения токена
     * @exception TokenNotGenerateException - кастоммное исключение на глобальном уровне, в случае невозможности генерации токена
     * @return {@link JwtToken}, содержащий сгенерированный токен и информацию о сроке его действия.
     */
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public JwtToken generateAndSaveToken(User user)
    {
        if (user == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент user! user равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            String token = jwtTokenUtil.generateToken(user); // Генерация токена
            JwtToken jwtToken = new JwtToken(); // Создание нового объекта JwtToken
            jwtToken.setUserId(user.getId()); // Установка идентификатора пользователя
            jwtToken.setToken(token); // Установка сгенерированного токена
            jwtToken.setExpirationDate(jwtTokenUtil.getExpirationDate(token)); // Установка даты истечения
            return jwtTokenRepository.save(jwtToken); // Сохранение токена в базе
        }
        catch (TokenNotGenerateException ex) // обрабатываем кастоммное исключение на глобальном уровне, в случае невозможности генерации токена
        {
            throw ex;
        }
        catch (TokenNotSaveException ex) // обрабатываем кастоммное исключение на глобальном уровне, в случае невозможности сохранения токена
        {
            throw ex;
        }
    }

    /**
     * Метод getTokenByUserId:
     * Получает JWT токен по идентификатору пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @exception UserNotFoundException - кастомное исключение, обработанное на глобальном уровне, в случае, если пользователь не найден
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return {@link Optional}, содержащий объект {@link JwtToken}, если токен существует.
     */
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public Optional<JwtToken> getTokenByUserId(Integer userId)
    {
        if (userId == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент userId! userId равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            return jwtTokenRepository.findByUserId(userId); // Поиск токена по идентификатору пользователя
        }
        catch (UserNotFoundException ex) // Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
        {
            throw ex;
        }
    }

    /**
     * Метод refreshToken:
     * Обновляет JWT токен для пользователя, если текущий токен истек.
     *
     * @param user Пользователь, для которого обновляется токен.
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception TokenNotDeleteException - кастоммное исключение обрабатываемое на глобальном уровне, в случае невозможности удаления токена
     * @return Новый объект {@link JwtToken}, если старый истек; иначе текущий токен.
     */
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public JwtToken refreshToken(User user)
    {
        if (user == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент user! user равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Optional<JwtToken> existingToken = getTokenByUserId(user.getId()); // Получение текущего токена пользователя
            if (existingToken.isPresent()) // Если токен существует, то:
            {
                JwtToken jwtToken = existingToken.get(); // получаем существующий токен

                if (jwtTokenUtil.isTokenExpired(jwtToken.getToken())) // Проверка, истек ли срок действия токена
                {
                    jwtTokenRepository.delete(jwtToken); // Удаление старого токена
                    return generateAndSaveToken(user); // Генерация нового токена
                }
                return jwtToken; // Возвращаем текущий токен, если он не истек
            }
            return null; // Если токен не найден, возвращаем null
        }
        catch (TokenNotDeleteException ex) //  обрабатываем кастоммное исключение на глобальном уровне, в случае невозможности удаления токена
        {
            throw ex;
        }
    }

    /**
     * Метод deleteExpiredTokens:
     * Удаляет все JWT токены с истекшим сроком действия.
     *
     * @exception TokenNotDeleteException - кастоммное исключение обрабатываемое на глобальном уровне, в случае невозможности удаления токена
     */
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public void deleteExpiredTokens()
    {
        try
        {
            Date now = new Date(); // Текущая дата
            List<JwtToken> expiredTokens = jwtTokenRepository.findByExpirationDateBefore(now); // Поиск истекших токенов
            for (JwtToken token : expiredTokens)
            {
                jwtTokenRepository.delete(token); // Удаление токенов
            }
        }
        catch (TokenNotDeleteException ex) //  обрабатываем кастоммное исключение на глобальном уровне, в случае невозможности удаления токена
        {
            throw ex;
        }
    }
    //endRegion
}
