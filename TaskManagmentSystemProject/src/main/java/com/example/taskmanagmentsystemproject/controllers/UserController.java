package com.example.taskmanagmentsystemproject.controllers;

import com.example.taskmanagmentsystemproject.dto.UserDTO;
import com.example.taskmanagmentsystemproject.dto.UserRegisterDTO;
import com.example.taskmanagmentsystemproject.models.transformer.GenericTransformer;
import com.example.taskmanagmentsystemproject.repository.UserRepository;
import com.example.taskmanagmentsystemproject.services.TaskService;
import com.example.taskmanagmentsystemproject.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Класс UserController:
 *
 * Реализует REST API для работы с пользователями в системе. Предоставляет endpoints для создания, обновления и получения информации о пользователях.
 *
 * Основные функции:
 * - Создание нового пользователя в системе.
 * - Получение пользователя по его идентификатору.
 * - Обновление информации о пользователе по его идентификатору.
 *
 * Зависимости:
 * - UserService: сервис для выполнения бизнес-логики, связанной с пользователями.
 * - UserRepository: репозиторий для работы с базой данных пользователей.
 * - GenericTransformer: трансформатор для преобразования между сущностями и DTO.
 * - TaskService: сервис для работы с задачами, используется для функционала, связанного с пользователями и их задачами.
 *
 * Методы:
 * - {@link #createNewUser(UserRegisterDTO)}: Создает нового пользователя в системе.
 * - {@link #getUserById(Integer)}: Получает информацию о пользователе по его идентификатору.
 * - {@link #updateUser(Integer, UserDTO)}: Обновляет информацию о пользователе по его идентификатору.
 *
 * Конечные точки:
 * - {@code POST /api/users/createUser}: Создает нового пользователя в системе.
 * - {@code GET /api/users/{id}}: Получает информацию о пользователе по его идентификатору.
 * - {@code POST /api/users/updateUser/{id}}: Обновляет информацию о пользователе по его идентификатору.
 *
 * Архитектурные особенности:
 * - Контроллер взаимодействует с бизнес-логикой через UserService.
 * - Использует аннотации Spring для маршрутизации запросов.
 * - Возвращает ответы клиенту в формате ResponseEntity.
 * - Работает с данными, представленными в формате UserDTO и UserRegisterDTO.
 *
 * HTTP-методы:
 * - POST: для создания и обновления пользователя.
 * - GET: для получения информации о пользователе.
 *
 * Исключения:
 * - Все исключения, возникающие при выполнении запросов, обрабатываются на уровне сервиса (UserService).
 *
 * Аннотации:
 * - @RestController: указывает, что класс является REST контроллером.
 * - @RequestMapping("/api/users"): задает базовый путь для всех endpoints в этом контроллере.
 */

@RestController
@RequestMapping("/api/users")
public class UserController
{
    //region Fields
    private UserService userService;
    private UserRepository userRepository;
    private GenericTransformer genericTransformer;

    private TaskService taskService;
    //endRegion

    //region Constructor
    public UserController(UserService userService, UserRepository userRepository, GenericTransformer genericTransformer,
                          TaskService taskService)
    {
        this.userService = userService;
        this.userRepository = userRepository;
        this.genericTransformer = genericTransformer;
        this.taskService = taskService;
    }
    //endRegion

    /**
     * Метод createNewUser - для создания нового пользователя.
     * Регистрирует нового пользователя в системе, обрабатывает данные из запроса и
     * сохраняет пользователя в базу данных.
     *
     * @param userRegisterDTO данные нового пользователя, переданные в теле запроса.
     * @return ResponseEntity<String> - ответ с сообщением о результатах регистрации,
     *         в случае успешной регистрации возвращается сообщение с успехом.
     */
    @PostMapping("/createUser")
    public ResponseEntity<String> createNewUser(@RequestBody UserRegisterDTO userRegisterDTO)
    {
        userService.registerUser(userRegisterDTO); // регистрация нового пользователя в БД
        return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно сохранён в базу данных" + userRegisterDTO);
    }


    /**
     * Метод getUserById - возвращает клиенту пользователя по его id.
     * Обрабатывает HTTP POST запросы по пути "/api/users/{id}".
     *
     * @param id идентификатор пользователя, переданный в url запроса.
     * @return ResponseEntity передача пользователя, либо о том что такого пользователя в БД нет.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id)
    {
        UserDTO userDTO = userService.getUserById(id); // Получение пользователя из базы данных и маппинг его в DTO
        return ResponseEntity.ok(userDTO); // Возврат DTO в JSON-формате в ответ пользователю
    }


    /**
     * Метод updateUser:
     * Обновляет информацию о пользователе по его ID.
     *
     * @param id идентификатор пользователя, которого нужно обновить.
     * @param userDTO объект, содержащий данные, которые нужно обновить для пользователя.
     * @return ResponseEntity с обновленным UserDTO.
     */
    @PostMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO)
    {
        userService.updateUser(id, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Обновление пользователя прошло успешно! " + userDTO);
    }

}

