package com.example.taskmanagmentsystemproject.controllers;

import com.example.taskmanagmentsystemproject.dto.TaskDTO;
import com.example.taskmanagmentsystemproject.mapper.TaskMapper;
import com.example.taskmanagmentsystemproject.models.task.TaskPriority;
import com.example.taskmanagmentsystemproject.models.task.TaskStatus;
import com.example.taskmanagmentsystemproject.models.transformer.GenericTransformer;
import com.example.taskmanagmentsystemproject.services.TaskService;
import com.example.taskmanagmentsystemproject.services.UserHelperService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Класс TaskController:
 *
 * Реализует REST API для работы с задачами в системе. Предоставляет endpoints для создания, обновления, получения, удаления задач,
 * а также для получения задач с фильтрацией и пагинацией.
 *
 * Основные функции:
 * - Создание новой задачи.
 * - Получение задачи по её идентификатору.
 * - Обновление задачи по её идентификатору.
 * - Удаление задачи по её идентификатору.
 * - Получение списка задач с фильтрацией и пагинацией.
 * - Получение задач текущего пользователя в зависимости от его роли (автор или исполнитель).
 * - Установка статуса и приоритета задачи.
 * - Назначение исполнителя и автора задаче.
 *
 * Зависимости:
 * - TaskService: сервис для выполнения бизнес-логики, связанной с задачами.
 * - GenericTransformer: трансформатор для преобразования между сущностями и DTO.
 * - TaskMapper: маппер для сопоставления полей между Task и TaskDTO.
 *
 * Методы:
 * - {@link #createTask(TaskDTO)}: Создает новую задачу.
 * - {@link #getTaskById(Integer)}: Получает задачу по её идентификатору.
 * - {@link #updateTask(Integer, TaskDTO)}: Обновляет задачу по её идентификатору.
 * - {@link #deleteTask(Integer)}: Удаляет задачу по её идентификатору.
 * - {@link #getAllTasks(String, String, TaskStatus, TaskPriority, int, int)}: Получает список задач с фильтрацией и пагинацией.
 * - {@link #getTasksForCurrentUser(String)}: Получает задачи для текущего пользователя, где он является автором или исполнителем.
 * - {@link #getTasksWhereUserIsAuthorAndExecutor()}: Получает задачи для текущего пользователя, где он является автором и исполнителем одновременно
 * - {@link #setTaskStatus(Integer, TaskStatus)} @PathVariable Integer id, @RequestParam TaskStatus status)}: Устанавливает статус задаче
 * - {@link #setTaskPriority(Integer, TaskPriority)} @PathVariable Integer id, @RequestParam TaskPriority priority)}: Устанавливает приоритет задаче
 * - {@link #setExecutorToTask(Integer, Integer)} @PathVariable Integer executorId, Integer taskId}: Устанавливает исполнителя задаче
 * - {@link #setAuthorToTask(Integer, Integer)} @PathVariable Integer authorId, Integer taskId}: Устанавливает автора задаче
 *
 * Конечные точки:
 * - {@code POST /api/tasks/create}: Создает новую задачу.
 * - {@code GET /api/tasks/getTask/{id}}: Получает задачу по её идентификатору.
 * - {@code PUT /api/tasks/update/{id}}: Обновляет задачу по её идентификатору.
 * - {@code DELETE /api/tasks/delete/{id}}: Удаляет задачу по её идентификатору.
 * - {@code GET /api/tasks/pagination}: Получает список задач с фильтрацией и пагинацией.
 * - {@code GET /api/tasks/my-tasks}: Получает задачи текущего пользователя в зависимости от его роли (автор или исполнитель).
 * - {@code GET /api/tasks/my-tasks/author-and-executor}: Получает задачи, где текущий пользователь является и автором, и исполнителем.
 * - {@code POST /api/tasks/{id}/setStatus}: Устанавливает статус задачи.
 * - {@code POST /api/tasks/{id}/setPriority}: Устанавливает приоритет задачи.
 * - {@code POST /api/tasks/{executorId}/{taskId}/setExecutorToTask}: Назначает исполнителя задаче.
 * - {@code POST /api/tasks/{authorId}/{taskId}/setAuthorToTask}: Назначает автора задаче.
 *
 * Архитектурные особенности:
 * - Контроллер взаимодействует с бизнес-логикой через TaskService.
 * - Использует аннотации Spring для маршрутизации запросов.
 * - Возвращает ответы клиенту в формате ResponseEntity.
 * - Работает с данными, представленными в формате TaskDTO.
 *
 ** HTTP-методы:
 *  * - POST: для создания задачи, обновления статуса и приоритета, назначения исполнителя и автора.
 *  * - GET: для получения задач, с фильтрацией и пагинацией, а также для получения информации по задачам текущего пользователя.
 *  * - PUT: для обновления существующих задач.
 *  * - DELETE: для удаления задач.
 *
 * Исключения:
 * - Все исключения, возникающие при выполнении запросов, обрабатываются на уровне сервиса (TaskService).
 *
 * Аннотации:
 * - @RestController: указывает, что класс является REST контроллером.
 * - @RequestMapping("/api/tasks"): задает базовый путь для всех endpoints в этом контроллере.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController
{
    //region Fields
    private TaskService taskService;
    private GenericTransformer genericTransformer;
    private TaskMapper taskMapper;
    private UserHelperService userHelperService;
    //endRegion

    //region Constructor
    public TaskController(TaskService taskService, GenericTransformer genericTransformer, TaskMapper taskMapper, UserHelperService userHelperService)
    {
        this.taskService =taskService;
        this.genericTransformer = genericTransformer;
        this.taskMapper = taskMapper;
        this.userHelperService =  userHelperService;
    }
    //endRegion

    //region Methods
    /**
     * Создает новую задачу.
     *
     * @param taskDTO объект DTO задачи, содержащий данные для создания задачи.
     * @return ResponseEntity<String> ответ с сообщением об успешном создании задачи и её данными.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody @Valid TaskDTO taskDTO)
    {
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO); // создание новой задачи и трансформация её в TaskDTO  для ответа пользователю
        return ResponseEntity.status(HttpStatus.OK).body("Новая задача успешно создана: " + createdTaskDTO); // ответ пользователю об успешно созданной новой задачи
    }

    /**
     * Метод getTaskById:
     * Предназначен для - получение задачи по её id
     * @param id - идентификационный номер задачи id
     * @return ResponseEntity<String> - подробный ответ пользователю со статусом OK, в случае успешного нахождения задачи по её id
     * **/
    @GetMapping("/getTask/{id}")
    public ResponseEntity<String> getTaskById(@PathVariable Integer id)
    {
        TaskDTO taskDTO = taskService.getTaskById(id);  // получение задачи по её id и трансформация Task в TaskDTO
        return ResponseEntity.status(HttpStatus.OK).body("Задача с id равным " + id + " найдена: " + taskDTO); // ответ пользователю об успешном нахождении задачи по её id
    }

    /**
     * Метод updateTask
     * Предназначен для - обновление задачи по её идентификационному номеру id
     * @param id - идентификационный номер задачи id
     * @return ResponseEntity<String> - подробный ответ пользователю со статусом OK, в случае успешного обновления задачи
     * **/
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Integer id, @RequestBody @Valid TaskDTO taskDTO)
    {
        TaskDTO updatedTaskDTO = taskService.updateTask(id, taskDTO); // получение обновлённой задачи, трансформируемой в TaskDTO для ответа пользователю
        return ResponseEntity.status(HttpStatus.OK).body("Задача с номером id равным: " + id + " успешно обновлена! " + updatedTaskDTO); // ответ пользователю об успешном обновлении задачи по её id
    }

    /**
     * Метод deleteTask:
     * Предназначен для - удаление задачи по её id
     * @param id - идентификационный номер задачи id
     * @return ResponseEntity<String> - подробный ответ пользователю со статусом OK, в случае успешного удаления задачи
     * **/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id)
    {
        taskService.deleteTask(id); // Используя сервис, удаляем задачу
        return ResponseEntity.status(HttpStatus.OK).body("Задача с номером id равным: " + id + " успешно удалена! "); // ответ пользователю об успешном удалении задачи по её id
    }

    /**
     * * Метод getAllTasks:
     * Предназначен для - получения списка задач с фильтрацией и пагинацией.
     *
     * @param authorEmail email автора задачи (опционально).
     * @param executorEmail email исполнителя задачи (опционально).
     * @param status статус задачи (опционально).
     * @param priority приоритет задачи (опционально).
     * @param page номер страницы для пагинации (по умолчанию 0).
     * @param size размер страницы для пагинации (по умолчанию 10).
     * @return ResponseEntity<Page<TaskDTO>> список задач с учетом фильтров и пагинации.
     */
    @GetMapping("/pagination")
    public ResponseEntity<Page<TaskDTO>> getAllTasks(
            @RequestParam(required = false) String authorEmail,
            @RequestParam(required = false) String executorEmail,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Page<TaskDTO> tasks = taskService.getTasks(authorEmail, executorEmail, status, priority, page, size); // Получаем задачи с применением фильтров и пагинации.

        return ResponseEntity.ok(tasks); // Возвращаем HTTP ответ с пагинированным списком задач.
    }


    /**
     * * Метод getTasksForCurrentUser:
     * Предназначен для - получения задачи для текущего пользователя, где он является автором или исполнителем.
     *
     * @param role роль текущего пользователя в задачах (author или executor, по умолчанию author).
     * @return ResponseEntity<String> положительный ответ пользователю со списком задач, соответствующих указанной роли.
     */
    @GetMapping("/my-tasks")
    public ResponseEntity<String> getTasksForCurrentUser(
            @RequestParam(value = "role", required = false, defaultValue = "author") String role)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Получаем текущего аутентифицированного пользователя.

        String email = authentication.getName();  // Извлекаем email текущего пользователя.

        List<TaskDTO> tasks; // инициализируем переменную для использования её в коде

        // Определяем, какие задачи возвращать в зависимости от роли пользователя:
        if ("author".equalsIgnoreCase(role)) // если роль пользователя совпадает с ролью author, то:
        {
            tasks = taskService.getTasksByAuthor(email); // Если пользователь автор, получаем его задачи как автора.
        }
        else if ("executor".equalsIgnoreCase(role)) // если роль пользователя совпадает с ролью executor, то:
        {
            tasks = taskService.getTasksByExecutor(email); // Если пользователь исполнитель, получаем его задачи как исполнителя.
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: В запросе указанна не корректная роль! Укажите корректную роль: ADMIN_ROLE либо USER_ROLE "); // Если указана некорректная роль, возвращаем HTTP ответ с ошибкой.
        }
        return ResponseEntity.status(HttpStatus.OK).body("Найден список задач у которых пользователь является исполнителем: " + tasks); // возвращаем положительный ответ об успешно найденном списке задач
    }

    /**
     * Метод getTasksWhereUserIsAuthorAndExecutor:
     * Предназначен для - получения задачи, для которых текущий пользователь является и автором, и исполнителем.
     *
     * @return ResponseEntity<String>  - положительный ответ со списком задач, где пользователь — автором и исполнителем одновременно.
     */
    @GetMapping("/my-tasks/author-and-executor")
    public ResponseEntity<String> getTasksWhereUserIsAuthorAndExecutor()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Получаем текущего аутентифицированного пользователя.

        String email = authentication.getName();  // Извлекаем email текущего пользователя.

        List<TaskDTO> tasks = taskService.getTasksByAuthorAndExecutor(email); // Получаем задачи, где текущий пользователь является и автором, и исполнителем.

        return ResponseEntity.status(HttpStatus.OK).body("Найден список задач у которых пользователь является автором и исполнителем: " + tasks); // Возвращаем ответ со списком задач.
    }

    /**
     * Метод setTaskStatus:
     * Предназначен для - установки статуса конкретной задачи, найденной по её id
     * @param id - идентификационный номер задачи id
     * @param status - статус задачи
     * @return ResponseEntity<String> -  ответ пользователю, при успешной установки статуса, с сообщением и статусом OK
     * **/
    @PostMapping("/{id}/setStatus")
    public ResponseEntity<String> setTaskStatus(@PathVariable Integer id, @RequestParam TaskStatus status)
    {
        TaskDTO taskDTO = taskService.setTaskStatus(id, status); // устанавливаем через сервис статус задаче
        return ResponseEntity.status(HttpStatus.OK).body("Статус: " + status + " успешно установлен задаче " + taskDTO + " с id: " + id); // возвращаем положительный ответ об успешно установленном статусе
    }

    /**
     * Метод setTaskPriority:
     * Предназначен для - установки приоритета конкретной задачи, найденной по её id
     * @param id - идентификационный номер задачи id
     * @param priority - приоритет задачи
     * @return ResponseEntity<String> -  ответ пользователю, при успешной установки приоритета, с сообщением и статусом OK
     * **/
    @PostMapping("/{id}/setPriority")
    public ResponseEntity<String> setTaskPriority(@PathVariable Integer id, @RequestParam TaskPriority priority)
    {
        TaskDTO taskDTO = taskService.setTaskPriority(id, priority); // устанавливаем через сервис приоритет задаче
        return ResponseEntity.status(HttpStatus.OK).body("Приоритет: " + priority + " успешно установлен задаче " + taskDTO + " с id: " + id); // возвращаем положительный ответ об успешно установленном приоритете
    }


    /**
     * Метод setExecutorToTask:
     * Предназначен для - назначения исполнителя задаче
     * @param executorId - идентификационный номер пользователя (исполнителя)
     * @param taskId - идентификационный номер задачи
     * @return ResponseEntity<String> - ответ пользователю, при успешной установки исполнителя задаче, с сообщением и статусом OK
     * **/
    @PostMapping("/{executorId}/{taskId}/setExecutorToTask")
    public ResponseEntity<String> setExecutorToTask(@PathVariable Integer executorId, Integer taskId)
    {
        taskService.assignExecutor(executorId, taskId); // используем сервис для назначения исполнителя задаче
        return ResponseEntity.status(HttpStatus.OK).body("Задаче с id равным " + taskId + " установлен исполнитель с id: " + executorId); // возвращаем положительный ответ об успешно установленном исполнителе
    }

    /**
     * Метод setAuthorToTask:
     * Предназначен для - назначения автора задаче
     * @param authorId - идентификационный номер пользователя (автора)
     * @param taskId - идентификационный номер задачи
     * @return ResponseEntity<String> - ответ пользователю, при успешной установки автора задаче, с сообщением и статусом OK
     * **/
    @PostMapping("/{authorId}/{taskId}/setAuthorToTask")
    public ResponseEntity<String> setAuthorToTask(@PathVariable Integer authorId, Integer taskId)
    {
        taskService.assignEAuthor(authorId, taskId); // используем сервис для назначения автора задаче
        return ResponseEntity.status(HttpStatus.OK).body("Задаче с id равным " + taskId + " установлен автор с id: " + authorId); // возвращаем положительный ответ об успешно установленном авторе
    }
    //ndRegion
}
