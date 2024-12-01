package com.example.taskmanagmentsystemproject.controllers;

import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.security.JwtTokenUtil;
import com.example.taskmanagmentsystemproject.services.CustomUserDetailService;
import com.example.taskmanagmentsystemproject.services.JwtTokenService;
import com.example.taskmanagmentsystemproject.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Класс AuthorizationController:
 *
 * Реализует REST API для авторизации и проверки прав доступа пользователей с использованием JWT токенов.
 * Предоставляет endpoint для проверки прав доступа пользователя на основе предоставленного токена.
 *
 * Основные функции:
 * - Проверка наличия прав доступа у пользователя на основе его JWT токена.
 * - Валидация токена и извлечение информации о пользователе.
 * - Проверка роли пользователя для разрешения или отказа в доступе.
 *
 * Зависимости:
 * - UserService: сервис для работы с пользователями и получения их информации.
 * - JwtTokenUtil: утилита для работы с JWT токенами.
 * - JwtTokenService: сервис для работы с JWT токенами.
 * - CustomUserDetailService: сервис для получения пользовательских данных (UserDetails) на основе email.
 *
 * Методы:
 * - {@link #checkAccess(String)}: Проверяет доступ пользователя на основе предоставленного JWT токена.
 *
 * Конечные точки:
 * - {@code POST /authz/checkAccess}: Проверяет права доступа пользователя, основываясь на его JWT токене.
 *
 * Архитектурные особенности:
 * - Контроллер взаимодействует с бизнес-логикой через UserService, JwtTokenUtil, и CustomUserDetailService.
 * - Использует аннотации Spring для маршрутизации запросов.
 * - Возвращает ответы пользователю в формате ResponseEntity в зависимости от результата проверки доступа.
 * - Обрабатывает JWT токены для аутентификации и авторизации пользователей.
 *
 * HTTP-методы:
 * - POST: для проверки прав доступа на основе токена.
 *
 * Исключения:
 * - Все исключения, возникающие при проверке токена или авторизации, обрабатываются на уровне сервиса.
 *
 * Аннотации:
 * - @RestController: указывает, что класс является REST контроллером.
 * - @RequestMapping("/authz"): задает базовый путь для всех endpoints в этом контроллере.
 */

@RestController
@RequestMapping("/authz") // Путь для авторизации
public class AuthorizationController
{
    //region Fields
    private final UserService userService; // Сервис для работы с пользователями.
    private final JwtTokenUtil jwtTokenUtil; // Утилита для работы с JWT токенами.
    private final JwtTokenService jwtTokenService; // Сервис для работы с JWT токенами.
    private CustomUserDetailService customUserDetailService; // Поле userDetails представляет информацию о пользователе, включая его учетные данные и роли, используемые для аутентификации и авторизации.
    //endregion

    //region Constructor
    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param userService     сервис для работы с пользователями.
     * @param jwtTokenUtil    утилита для работы с JWT токенами.
     * @param jwtTokenService сервис для работы с JWT токенами.
     */
    public AuthorizationController(UserService userService, JwtTokenUtil jwtTokenUtil,
                                   JwtTokenService jwtTokenService, CustomUserDetailService customUserDetailService)
    {
        this.userService = userService; // Инициализируем поле userService.
        this.jwtTokenUtil = jwtTokenUtil; // Инициализируем поле jwtTokenUtil.
        this.jwtTokenService = jwtTokenService; // Инициализируем поле jwtTokenService.
        this.customUserDetailService = customUserDetailService; // Поле userDetails представляет информацию о пользователе
    }
    //endregion

    //region Methods
    /**
     * Метод checkAccess:
     * Проверяет, есть ли у пользователя права доступа на выполнение операции на основе JWT токена.
     * Авторизует пользователя и проверяет его роль (например, "ADMIN_ROLE" или "USER_ROLE").
     *
     * @param token токен пользователя для авторизации.
     * @return сообщение о доступе или отказе в доступе.
     */
    @PostMapping("/checkAccess")
    public ResponseEntity<String> checkAccess(@RequestHeader("Authorization") String token) // извлекаем токен из заголовка с ключом "Authorization"
    {
        // Извлекаем токен пользователя:
        String jwtToken = token.replace("Bearer ", ""); // Убираем префикс "Bearer " из токена.


        String userEmail = jwtTokenUtil.extractUsername(jwtToken); // Извлекаем имя пользователя (email) из токена:

        if (userEmail == null)  // если email пользователя рана null, то:
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный токен!"); // отправляем ответ пользователю о неверном токене
        }

        UserDetails userDetails = customUserDetailService.loadUserByUsername(userEmail); // получаем UserDetails

        // Проверяем, валиден ли токен:
        if (!jwtTokenUtil.isTokenValid(jwtToken, userDetails)) // Если токен не валиден:
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный токен!"); // Возвращаем ошибку.
        }

        User user = userService.getUserByUserName(userEmail); // получаем пользователя по его email

        // Извлекаем роль пользователя из токена:
        String role = String.valueOf(user.getUserRole()); // Извлекаем роль пользователя из токена.

        // Проверяем, есть ли у пользователя необходимые права (например, роль "ADMIN"):
        if ("ADMIN_ROLE".equals(role)) // Если роль пользователя соответствует "ADMIN":
        {
            return ResponseEntity.status(HttpStatus.OK).body("Доступ разрешен"); // Разрешаем доступ.
        }
        if("USER_ROLE".equals(role))
        {
            // В случае, если у пользователя нет необходимых прав:
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещен!"); // Отказываем в доступе.
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такой роли не найдено! Доступ запрещен!"); // Отказываем в доступе.
    }
    //endregion
}
