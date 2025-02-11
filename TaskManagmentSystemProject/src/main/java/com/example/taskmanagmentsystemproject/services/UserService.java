package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.dto.UserDTO;
import com.example.taskmanagmentsystemproject.dto.UserRegisterDTO;
import com.example.taskmanagmentsystemproject.exceptions.customExeptions.*;
import com.example.taskmanagmentsystemproject.mapper.UserMapper;
import com.example.taskmanagmentsystemproject.mapper.UserRegisterMapper;
import com.example.taskmanagmentsystemproject.models.transformer.GenericTransformer;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.models.user.UserRole;
import com.example.taskmanagmentsystemproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Класс UserService:
 *
 * Реализует бизнес-логику для работы с пользователями в системе. Предоставляет методы для регистрации, обновления, аутентификации пользователей,
 * получения пользователей по различным критериям и проверки их данных.
 *
 * Зависимости:
 * - UserRepository: репозиторий для работы с базой данных пользователей.
 * - PasswordEncoder: кодировщик для безопасного хранения паролей.
 * - TaskService: сервис для работы с задачами, связанными с пользователями.
 * - GenericTransformer: универсальный трансформатор для преобразования сущностей и DTO.
 * - UserMapper: маппер для сопоставления полей между User и UserDTO.
 * - UserRegisterMapper: маппер для сопоставления полей между User и UserRegisterDTO.
 *
 * Методы:
 * - {@link #registerUser(UserRegisterDTO)}: Регистрирует нового пользователя, проверяет данные, кодирует пароль и сохраняет в базу данных.
 * - {@link #userToUserDTO(User)}: Преобразует объект User в объект UserDTO.
 * - {@link #getUserById(Integer)}: Находит пользователя по ID и преобразует его в UserDTO.
 * - {@link #updateUser(Integer, UserDTO)}: Обновляет данные пользователя по ID.
 * - {@link #checkValidDate(UserRegisterDTO)}: Проверяет валидность данных пользователя при регистрации (например, длина пароля).
 * - {@link #authenticate(String, String)}: Аутентифицирует пользователя по email и паролю.
 * - {@link #getUserByUserName(String)}: Находит пользователя по email.
 *
 * Исключения:
 * - {@link PasswordEmptyException}: выбрасывается, если пароль отсутствует или пустой.
 * - {@link PasswordSizeException}: выбрасывается, если длина пароля меньше минимально допустимой.
 * - {@link UserNotFoundException}: выбрасывается, если пользователь не найден.
 * - {@link FailTransformException}: выбрасывается при ошибке преобразования между сущностями и DTO.
 * - {@link UserNotSaveException}: выбрасывается при невозможности сохранить пользователя в базе данных.
 * - {@link IllegalArgumentException}: стандартное системное исключение выбрасывается, если переданы некорректные аргументы
 * - {@link RuntimeException}: стандартное системное исключение выбрасывается, если сохранённый пароль не совпал с переданным
 *
 * Аннотации:
 * - @Service: указывает, что класс является сервисом и управляется Spring-контейнером.
 * - @Transactional: обеспечивает атомарность выполнения операций, предотвращая их частичное выполнение.
 */
@Service
public class UserService
{
    //region Fields
    private UserRepository userRepository;  // Репозиторий для работы с базой данных пользователей
    private PasswordEncoder passwordEncoder; // Кодировщик паролей
    private final GenericTransformer genericTransformer = new GenericTransformer(); // Универсальный трансформер для преобразования сущностей и DTO
    private TaskService taskService; // сервис задач
    private UserMapper userMapper; // // Трансформатор сущностей (User и UserDTO)
    private UserRegisterMapper userRegisterMapper; // Трансформатор сущностей (User  и UserRegisterDTO)
    //endRegion

    //region Constructor
    /**
     * Конструктор для инициализации сервиса с репозиторием и кодировщиком паролей.
     *
     * @param userRepository репозиторий для работы с данными пользователей.
     * @param passwordEncoder кодировщик паролей для безопасного хранения.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TaskService taskService, UserMapper userMapper, UserRegisterMapper userRegisterMapper)
    {
        this.userRepository = userRepository; // Инициализация репозитория
        this.passwordEncoder = passwordEncoder; // Инициализация кодировщика паролей
        this.taskService =  taskService; // Инициализация TaskService
        this.userMapper = userMapper; // Инициализация UserMapper
        this.userRegisterMapper = userRegisterMapper; // Инициализация UserRegisterMapper
    }
    //endRegion

    //region Methods
    /**
     * Метод для аутентификации пользователя
     * Используется в классе AuthController, в методе login(@RequestBody LoginRequestDTO loginRequest), ендпоинт: localhost:8080/auth/login.
     * Проверяет, существует ли пользователь с указанным email и совпадает ли введенный пароль с сохраненным.
     *
     * @param email    адрес электронной почты пользователя.
     * @param password пароль пользователя для проверки.
     * @exception  IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception UserNotFoundException - кастомное исключение, обрабатывается на глобальном уровне, в случае ненахождения пользователя в базе данных
     * @exception RuntimeException - Системное исключение, выбрасывается в случае несовпадения введённого пароля с сохранённым
     * @return возвращает найденного пользователя, если данные корректны, или null, если аутентификация не прошла.
     */
    @Transactional // Обеспечиваем атомарность операций в этом методе
    public User authenticate(String email, String password)
    {
        if (email == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент email! email равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        if (password == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент password! password равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }

        User user = userRepository.findByEmail(email); // Ищем пользователя по email

        if(user == null) // Выбрасываем исключение, если пользователь не найден
        {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) // Проверяем, совпадает ли введенный пароль с сохраненным
        {
            throw new RuntimeException("Не верный пароль"); // Выбрасываем исключение, если пароли не совпадают
        }
        return user; // Возвращаем пользователя, если аутентификация прошла успешно
    }

    /**
     * Метод registerUser:
     * Служит для регистрации пользователя.
     * Преобразует DTO пользователя в сущность, проверяет данные, кодирует пароль и сохраняет пользователя в базу данных.
     * В случае ошибки при трансформации, кодировании пароля или сохранении пользователя выбрасываются соответствующие исключения.
     *
     * @param userRegisterDTO данные пользователя для регистрации. Содержит информацию о пользователе,
     *                        включая пароль, который будет преобразован и закодирован перед сохранением в базе данных.
     * @return возвращает зарегистрированного пользователя с успешно закодированным паролем и сохраненного в базе данных.
     * @exception  FailTransformException если происходит ошибка при преобразовании DTO в сущность.
     * @exception  UserNotSaveException если пользователь не может быть сохранен в базе данных.
     * @exception  FailEncodePasswordException если возникает ошибка при кодировании пароля пользователя.
     * @exception  IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     */
    @Transactional // Обеспечиваем атомарность (неделимость) операций в этом методе
    public User registerUser(UserRegisterDTO userRegisterDTO)
    {
        if (userRegisterDTO == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент userRegisterDTO! userRegisterDTO равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        checkValidDate(userRegisterDTO); // Проверка валидности данных (например, пароль не должен быть пустым или слишком коротким)

        try
        {
            User user = genericTransformer.transformFromDTOToEntity(userRegisterDTO, userRegisterMapper); // Преобразуем DTO пользователя в сущность User (с помощью кастомного универсального преобразователя)

            System.out.println("печать пользователя из сервиса метода registerAdmin из блока try catch: " + user); // Логирование созданного объекта пользователя

            user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword())); // Кодируем пароль пользователя

            System.out.println("Печать зашифрованного пароля: " + user.getPassword()); // Логирование зашифрованного пароля

            userRepository.save(user); // Сохраняем пользователя в базе данных

            return user; // Возвращаем пользователя после успешной регистрации
        }
        catch (FailTransformException ex)
        {
            throw ex; // Логируем исключение на глобальном уровне о невозможности трансформации одного объекта в другой
        }
        catch (UserNotSaveException ex)
        {
            throw  ex; // // Логируем исключение на глобальном уровне  о невозможности сохранения объекта в базе данных
        }
        catch (FailEncodePasswordException ex)
        {
            throw  ex; // Логируем исключение на глобальном уровне  о невозможности кодировки пароля
        }
    }

    /**
     * Метод userToUserDTO
     * Трансформирует объект User в объект UserDTO
     *
     * @param user - объект класса User, пользователь
     * @exception FailTransformException - кастомное исключение обработанное на глобальном уровне, невозможность маппинга одного объекта в другой
     * @exception  IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return userDTO - объект класса userDTO
     * **/
    public UserDTO userToUserDTO(User user)
    {
        if (user == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент user! user равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            UserDTO userDTO = genericTransformer.transformFromEntityToDTO(user, userMapper); // Преобразуем найденного пользователя в DTO
            return userDTO; // возвращаем объект UserDTO
        }
        catch (FailTransformException ex) // кастомное исключение обработанное на глобальном уровне, невозможность маппинга одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод getUserById:
     * Предназначен для получения пользователя по ID и преобразования его в DTO.
     *
     * @param id идентификатор пользователя.
     * @return UserDTO - возвращает объект трансформированный для ответа объект UserDTO.
     * @exception UserNotFoundException  - кастомное исключение, обрабатывается на глобальном уровне, в случае ненахождения пользователя в базе данных
     * @exception FailTransformException - кастомное исключение, обрабатывается на глобальном уровне, в случае невозможности трансформировать объект User в UserDTO
     * @exception  IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     */
    @Transactional // Обеспечиваем атомарность операций в этом методе
    public UserDTO getUserById(Integer id)
    {
        if (id == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            User user = userRepository.findUserById(id); // Находим пользователя по ID в базе данных
            UserDTO userDTO = genericTransformer.transformFromEntityToDTO(user, userMapper); // Преобразуем найденного пользователя в DTO
            return userDTO; // Возвращаем DTO пользователя
        }
        catch (UserNotFoundException ex) // кастомное исключение, обрабатывается на глобальном уровне, в случае ненахождения пользователя в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // кастомное исключение, обрабатывается на глобальном уровне, в случае невозможности трансформировать объект User в UserDTO
        {
            throw ex;
        }
    }

    /**
     * Метод updateUser:
     * Предназначен для обновления пользователя по его id
     * @param id - идентификатор id пользователя
     * @param userDTO - трансформированный объект пользователя, созданный для передачи пользователя в запросах
     * @exception UserNotSaveException - кастомное исключение, обрабатывается на глобальном уровне, в случае несохранения пользователя в базе данных
     * @exception UserNotFoundException - кастомное исключение, обрабатывается на глобальном уровне, в случае ненахождения пользователя в базе данных
     * @exception  IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return UserDTO - возвращаем трансформированный объект пользователя, созданный для передачи пользователя в запросах
     * **/
    public UserDTO updateUser(Integer id, UserDTO userDTO)
    {
        if (id == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        if (userDTO == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент userDTO! userDTO равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            User updatedUser = userRepository.findUserById(id); // Получаем пользователя по его ID из базы данных.

            updatedUser.setEmail(userDTO.getEmail()); // Обновляем email пользователя на новое значение из DTO.


            if(userDTO.getUserRole() != null && !userDTO.getUserRole().isEmpty())
            {
                updatedUser.setUserRole(UserRole.valueOf(userDTO.getUserRole())); // Обновляем роль пользователя на новую, преобразуя строку в перечисление UserRole.
            }
            else
            {
                updatedUser.setUserRole(updatedUser.getUserRole()); // оставляем роль прежней
            }


            // Если список созданных задач не равен null, то обрабатываем его.
            if (userDTO.getCreatedTasks() != null)
            {
                updatedUser.setCreatedTasks(userDTO.getCreatedTasks()); // Обновляем созданные задачи пользователя.
            }

            // Если список назначенных задач не равен null, то обрабатываем его.
            if (userDTO.getAssignedTasks() != null)
            {
                updatedUser.setAssignedTasks(userDTO.getAssignedTasks()); // Обновляем назначенные задачи пользователя.

            }
            userRepository.save(updatedUser); // Сохраняем обновленного пользователя в базу данных.

            userDTO = userToUserDTO(updatedUser); // Преобразуем обновленного пользователя в DTO для ответа клиенту.

            return userDTO; // Возвращаем успешный ответ с обновленным пользователем в формате DTO.
        }
        catch (UserNotSaveException ex) // кастомное исключение, обрабатывается на глобальном уровне, в случае несохранения пользователя в базе данных
        {
            throw ex;
        }
        catch (UserNotFoundException ex) // кастомное исключение, обрабатывается на глобальном уровне, в случае ненахождения пользователя в базе данных
        {
            throw  ex;
        }

    }

    /**
     * Метод для проверки валидности данных пользователя при регистрации.
     * Проверяет, чтобы пароль не был пустым и длина пароля была не менее 5 символов.
     *
     * @param userRegisterDTO данные пользователя для регистрации.
     * @exception  IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception PasswordEmptyException - кастомное исключение, обрабатывается на глобальном уровне, в случае пароля равным нулю
     * @exception PasswordSizeException - кастомное исключение, обрабатывается на глобальном уровне, в случае некорректно введённой длины пароля
     */
    public void checkValidDate(UserRegisterDTO userRegisterDTO)
    {
        if (userRegisterDTO == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент userRegisterDTO! userRegisterDTO равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }

        if(userRegisterDTO.getPassword() == null || userRegisterDTO.getPassword().isEmpty())  // Проверка на пустой пароль
        {
            throw new PasswordEmptyException("Введите пароль верно! Пароль не может быть пустым!"); // Исключение, если пароль пустой
        }

        if(userRegisterDTO.getPassword().length() < 5) // Проверка на короткий пароль
        {
            throw new PasswordSizeException("Введите пароль верно! Пароль не может содержать менее пяти символов! "); // Исключение, если пароль слишком короткий
        }
    }


    /**
     * Метод getUserByUserName:
     * Находит пользователя по его email
     * @param email - email пользователя, являеться его именем для идентификации и авторизации
     * @exception UserNotFoundException - кастомное исключение, обрабатывается на глобальном уровне, в случае ненахождения пользователя в базе данных
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * **/
    public User getUserByUserName(String email)
    {
        if (email == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент email! email равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }

        User user = userRepository.findByEmail(email); // получения пользователя по его email

        if(user == null) // если пользователь равен null, то:
        {
               throw new UsernameNotFoundException("Пользователь с email " + email + " не найден!"); // кастомное исключение, обрабатывается на глобальном уровне, в случае ненахождения пользователя в базе данных
        }
        return user; // возвращаем пользователя
    }
    //endRegion
}
