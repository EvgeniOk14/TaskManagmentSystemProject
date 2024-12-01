package com.example.taskmanagmentsystemproject.controllers;

import com.example.taskmanagmentsystemproject.dto.LoginRequestDTO;
import com.example.taskmanagmentsystemproject.models.token.JwtToken;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.security.JwtTokenUtil;
import com.example.taskmanagmentsystemproject.services.JwtTokenService;
import com.example.taskmanagmentsystemproject.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

/**
 * Класс AuthController:
 *
 * Реализует REST API для аутентификации пользователей и работы с JWT токенами.
 * Предоставляет endpoint для входа пользователя с использованием учетных данных (email и пароль)
 * и возвращает JWT токен при успешной аутентификации.
 *
 * Основные функции:
 * - Аутентификация пользователя по email и паролю.
 * - Генерация и обновление JWT токенов для авторизованных пользователей.
 * - Возврат JWT токена при успешной аутентификации или сообщение об ошибке при неудачной попытке входа.
 *
 * Зависимости:
 * - AuthenticationManager: менеджер для аутентификации пользователей.
 * - JwtTokenUtil: утилита для работы с JWT токенами.
 * - UserService: сервис для работы с пользователями, включая аутентификацию.
 * - JwtTokenService: сервис для генерации и работы с JWT токенами.
 *
 * Методы:
 * - {@link #login(LoginRequestDTO)}: Аутентифицирует пользователя и возвращает JWT токен.
 *
 * Конечные точки:
 * - {@code POST /auth/login}: Аутентифицирует пользователя и возвращает JWT токен.
 *
 * Архитектурные особенности:
 * - Контроллер взаимодействует с бизнес-логикой через UserService и JwtTokenService.
 * - Использует аннотации Spring для маршрутизации запросов.
 * - Возвращает JWT токен в случае успешной аутентификации или сообщение об ошибке.
 * - Использует DTO для приема учетных данных пользователя.
 *
 * HTTP-методы:
 * - POST: для аутентификации пользователя и получения JWT токена.
 *
 * Исключения:
 * - Все исключения, возникающие при аутентификации, обрабатываются на уровне сервиса (UserService).
 *
 * Аннотации:
 * - @RestController: указывает, что класс является REST контроллером.
 * - @RequestMapping("/auth"): задает базовый путь для всех endpoints в этом контроллере.
 */

@RestController
@RequestMapping("/auth")
public class AuthController
{
    //region Fields
    private final AuthenticationManager authenticationManager; // Менеджер аутентификации.
    private final JwtTokenUtil jwtTokenUtil; // Утилита для работы с JWT токенами.
    private final UserService userService; // Сервис для работы с пользователями.
    private final JwtTokenService jwtTokenService; // Сервис для работы с JWT токенами.
    //endregion

    //region Constructor
    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param authenticationManager менеджер аутентификации.
     * @param jwtTokenUtil          утилита для работы с JWT токенами.
     * @param userService           сервис для работы с пользователями.
     * @param jwtTokenService       сервис для работы с JWT токенами.
     */
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                          UserService userService, JwtTokenService jwtTokenService)
    {
        this.authenticationManager = authenticationManager; // Инициализируем поле authenticationManager.
        this.jwtTokenUtil = jwtTokenUtil; // Инициализируем поле jwtTokenUtil.
        this.userService = userService; // Инициализируем поле userService.
        this.jwtTokenService = jwtTokenService; // Инициализируем поле jwtTokenService.
    }
    //endregion

    //region Methods
    /**
     * Метод login:
     * Выполняет аутентификацию пользователя с помощью предоставленных учетных данных (email и пароль)
     * и возвращает JWT токен, если аутентификация успешна.
     *
     * @param loginRequest объект DTO, содержащий email и пароль пользователя.
     * @return строка с JWT токеном в случае успешной аутентификации или сообщение об ошибке.
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequest)
    {
        // Извлекаем email и password из тела запроса loginRequest:
        String email = loginRequest.getEmail(); // Извлекаем email из тела запроса loginRequest
        String password = loginRequest.getPassword(); // Извлечение пароля из тела запроса loginRequest

        // Аутентификация пользователя:
        User user = userService.authenticate(email, password); // Аутентифицируем пользователя с помощью сервиса.

        if (user != null) // Если пользователь найден и аутентифицирован, то:
        {
            // Обновляем или генерируем новый токен для пользователя:
            JwtToken jwtToken = jwtTokenService.refreshToken(user); // Пытаемся обновить существующий токен.
            if (jwtToken == null) // Если обновление токена невозможно (например, он равен null, он не истек), то:
            {
                jwtToken = jwtTokenService.generateAndSaveToken(user); // Генерируем новый токен.
            }
            return jwtToken.getToken(); // Возвращаем сгенерированный токен.
        }
        return "Неверные учетные данные!"; // Если аутентификация не удалась, возвращаем сообщение об ошибке.
    }
    //endregion
}

