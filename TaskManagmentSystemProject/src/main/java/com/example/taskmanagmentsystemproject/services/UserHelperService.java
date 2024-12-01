package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.exceptions.customExeptions.UserNotFoundException;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Класс UserHelperService:
 * (вспомогательный класс - сервис, необходим для исключение циклической зависимости между User, Task и Comments, при внедрении UserService)
 * Реализует вспомогательную бизнес-логику для работы с пользователями в приложении.
 * Предоставляет методы для поиска пользователей, авторов и исполнителей по email или идентификатору.
 *
 * Ключевые функции:
 * - Получение объекта {@link User} по email.
 * - Получение объекта {@link User} по идентификатору.
 *
 * Зависимости:
 * - {@link UserRepository}: для доступа к данным пользователей в базе.
 *
 * Методы:
 * - {@link #getAuthorByEmail(String)}: Находит автора по указанному email.
 * - {@link #getExecutorByEmail(String)}: Находит исполнителя по указанному email.
 * - {@link #getUserByEmail(String)}: Находит пользователя по указанному email.
 * - {@link #getUserById(Integer)}: Находит пользователя по указанному идентификатору.
 *
 * Исключения:
 * - {@link UserNotFoundException}: Выбрасывается кастомное исключение, если пользователь не найден в базе данных.
 * - {@link IllegalArgumentException}: Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
 *
 * Аннотации:
 * - @Service: указывает, что класс является сервисным компонентом в Spring.
 */
@Service
public class UserHelperService
{
    //region Fields
    private final UserRepository userRepository; // Репозиторий для работы с пользователями
    //endRegion

    //region Constructor
    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param userRepository Репозиторий для доступа к данным пользователей.
     */
    public UserHelperService(UserRepository userRepository)
    {
        this.userRepository = userRepository; // Инициализация репозитория
    }
    //endRegion

    //region Methods
    /**
     * Метод getAuthorByEmail:
     * Находит автора по указанному email.
     *
     * @param email Email автора.
     * @exception IllegalArgumentException - Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception UserNotFoundException - Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
     * @return {@link User}, представляющий автора.
     */
    public User getAuthorByEmail(String email)
    {
        if (email == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент email! email равен null."); // Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            return userRepository.findByEmail(email); // Ищем пользователя-автора по email
        }
        catch (UserNotFoundException ex) // Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
        {
            throw ex;
        }
    }

    /**
     * Метод getExecutorByEmail:
     * Находит исполнителя по указанному email.
     *
     * @param executorEmail Email исполнителя.
     * @exception IllegalArgumentException - Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception UserNotFoundException - Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
     * @return {@link User}, представляющий исполнителя.
     */
    public User getExecutorByEmail(String executorEmail)
    {
        if (executorEmail == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент executorEmail! executorEmail равен null."); // Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            return userRepository.findByEmail(executorEmail); // Ищем пользователя-исполнителя по email
        }
        catch (UserNotFoundException ex) // Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
        {
            throw ex;
        }
    }

    /**
     * Метод getUserByEmail:
     * Находит пользователя по указанному email.
     *
     * @param email Email пользователя.
     * @exception IllegalArgumentException - Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception UserNotFoundException - Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
     * @return {@link User}, представляющий пользователя.
     */
    public User getUserByEmail(String email)
    {
        if (email == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент email! email равен null."); // Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            return userRepository.findByEmail(email); // Ищем пользователя по email
        }
        catch (UserNotFoundException ex) // Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
        {
            throw ex;
        }
    }

    /**
     * Метод getUserById:
     * Находит пользователя по указанному идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @exception IllegalArgumentException - Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception UserNotFoundException - Если пользователь не найден, выбрасываем кастомное исключение, обработанное на глобальном уровне
     * @return {@link User}, представляющий пользователя.
     */
    public User getUserById(Integer id)
    {
        if (id == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // Выбрасываем системное исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            return userRepository.findUserById(id); // Ищем пользователя по идентификатору
        }
        catch (UserNotFoundException ex) // // Выбрасываем кастомное исключение, обрабатываемое на глобальном уровне, в случае, если пользователь не был найден в базу данных
        {
            throw ex;
        }
    }
    //endRegion
}
