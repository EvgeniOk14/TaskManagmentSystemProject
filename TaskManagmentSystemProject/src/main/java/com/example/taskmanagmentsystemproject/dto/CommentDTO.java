package com.example.taskmanagmentsystemproject.dto;

import com.example.taskmanagmentsystemproject.models.task.Task;
import com.example.taskmanagmentsystemproject.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс CommentDTO:
 *
 * Представляет объект передачи данных (DTO) для комментариев.
 * Содержит информацию о тексте комментария, а также связи с задачами и пользователями.
 *
 * Основные атрибуты:
 * - Уникальный идентификатор комментария (id).
 * - Текст комментария (text).
 * - Связь с задачей, к которой относится комментарий (task).
 * - Связь с пользователем, который оставил комментарий (user).
 *
 * Аннотации:
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 *
 * Поля:
 * - {@link #id}: уникальный идентификатор комментария.
 * - {@link #text}: текст комментария.
 * - {@link #task}: задача, к которой относится комментарий.
 * - {@link #user}: пользователь, оставивший комментарий.
 */
@Data // библиотека Lombok: добавляет стандартные методы (геттеры, сеттеры и т.д.)
@AllArgsConstructor // Конструктор со всеми аргументами
@NoArgsConstructor  // Конструктор без аргументов
public class CommentDTO
{
    //region Fields
    /**
     * Уникальный идентификатор комментария.
     * Используется как первичный ключ в таблице комментариев.
     */
    @Id
    private Integer id; // Уникальный идентификатор комментария.

    /**
     * Текст комментария.
     * Поле хранит содержимое комментария, добавленного пользователем.
     * Обязательное поле.
     */
    @NotEmpty(message = "Текст комментария не должен быть пустым.")
    private String text; // Поле для хранения текста комментария.

    /**
     * Задача, к которой относится комментарий.
     * Поле "task_id" в базе данных связывает комментарий с конкретной задачей.
     */
    private Task task; // Связь с задачей.

    /**
     * Пользователь, оставивший комментарий.
     * Поле "user_id" в базе данных связывает комментарий с конкретным пользователем.
     */
    private User user; // Связь с пользователем.
    //endRegion
}
