package com.example.taskmanagmentsystemproject.dto;

import com.example.taskmanagmentsystemproject.models.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Класс UserDTO:
 *
 * Представляет объект передачи данных (DTO) для пользователя в системе.
 * Используется для передачи информации о пользователе, включая уникальный идентификатор, роль, а также связанные задачи.
 *
 * Основные атрибуты:
 * - Уникальный идентификатор пользователя (id).
 * - Роль пользователя (userRole).
 * - Список задач, созданных пользователем (createdTasks).
 * - Список задач, назначенных пользователю (assignedTasks).
 *
 * Аннотации:
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 * - @JsonIgnore: исключает из сериализации поле, над которым поставлена данная аннотация, чтобы избежать рекурсии
 *
 * Поля:
 * - {@link #id}: уникальный идентификатор пользователя.
 * - {@link #userRole}: роль пользователя (например, ADMIN_ROLE, USER_ROLE).
 * - {@link #createdTasks}: список задач, созданных пользователем.
 * - {@link #assignedTasks}: список задач, назначенных пользователю.
 *
 * Валидация:
 * - Нет явной валидации в этом классе, так как используется базовый класс {@link BaseUserDTO}.
 */
@Data // библиотека Lombok: добавляет стандартные методы (геттеры, сеттеры и т.д.)
@AllArgsConstructor // Конструктор со всеми аргументами
@NoArgsConstructor // Конструктор без аргументов
public class UserDTO extends BaseUserDTO
{
    //region Fields
    /**
     * Уникальный идентификатор пользователя.
     * Используется для однозначной идентификации пользователя в системе.
     */
    private Integer id; // Уникальный идентификатор пользователя.

    /**
     * Роль пользователя в системе.
     * Например, ADMIN_ROLE для администратора и USER_ROLE для обычного пользователя.
     */
    private String userRole; // Роль пользователя в системе.

    /**
     * Список задач, созданных пользователем.
     * Связь с задачами через поле "author_id" в таблице "task_table".
     */
    private List<Task> createdTasks; // Список задач, созданных пользователем.

    /**
     * Список задач, назначенных пользователю.
     * Связь с задачами через поле "executor_id" в таблице "task_table".
     */
    @JsonIgnore // Эта аннотация исключит из сериализации поле с задачами, чтобы избежать рекурсии
    private List<Task> assignedTasks; // Список задач, назначенных пользователю.
    //endRegion
}


