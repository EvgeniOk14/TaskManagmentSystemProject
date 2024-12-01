package com.example.taskmanagmentsystemproject.dto;

import com.example.taskmanagmentsystemproject.models.comment.Comment;
import com.example.taskmanagmentsystemproject.models.task.TaskPriority;
import com.example.taskmanagmentsystemproject.models.task.TaskStatus;
import com.example.taskmanagmentsystemproject.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Класс TaskDTO:
 *
 * Представляет объект передачи данных (DTO) для задачи.
 * Используется для передачи информации о задаче, включая заголовок, описание, статус, приоритет и связанный пользовательский контекст.
 *
 * Основные атрибуты:
 * - Уникальный идентификатор задачи (id).
 * - Заголовок задачи (title).
 * - Описание задачи (description).
 * - Статус задачи (status).
 * - Приоритет задачи (priority).
 * - Автор задачи (author).
 * - Исполнитель задачи (executor).
 * - Список комментариев, связанных с задачей (listOfComments).
 *
 * Аннотации:
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 * - @Id: указывает, что поле является уникальным идентификатором (первичным ключом).
 * - @NotNull: указывает, что поле не может быть пустым.
 *
 * Поля:
 * - {@link #id}: уникальный идентификатор задачи.
 * - {@link #title}: заголовок задачи, обязательное поле.
 * - {@link #description}: описание задачи, необязательное поле.
 * - {@link #status}: статус задачи, обязательное поле.
 * - {@link #priority}: приоритет задачи, обязательное поле.
 * - {@link #author}: автор задачи, обязательное поле.
 * - {@link #executor}: исполнитель задачи, необязательное поле.
 * - {@link #listOfComments}: список комментариев, связанных с задачей.
 *
 * Валидация:
 * - Поле `title`, `status`, `priority` обязаны быть заполнены (проверка с помощью аннотаций {@link NotNull}).
 * - Поле `listOfComments` связано с другими сущностями через каскадное удаление.
 */
@Data // библиотека Lombok: добавляет стандартные методы (геттеры, сеттеры и т.д.)
@AllArgsConstructor // Конструктор со всеми аргументами
@NoArgsConstructor // Конструктор без аргументов
public class TaskDTO
{
    //region Fields
    /**
     * Уникальный идентификатор задачи.
     * Связан с первичным ключом в базе данных.
     */
    @Id
    private Integer id; // Уникальный идентификатор задачи (первичный ключ в базе данных).

    /**
     * Заголовок задачи.
     * Обязательное поле.
     */
    @NotNull(message = "Поле title не должно быть пустым!")
    private String title; // Заголовок задачи. Обязательное поле.

    /**
     * Подробное описание задачи.
     * Необязательное поле.
     */
    private String description; // Подробное описание задачи. Поле необязательно.

    /**
     * Статус задачи.
     * Обязательное поле. Может быть PENDING, IN_PROGRESS, COMPLETED.
     */
    @NotNull(message = "Поле status не должно быть пустым!")
    private TaskStatus status; // Текущий статус задачи (например, PENDING, IN_PROGRESS, COMPLETED). Обязательное поле.

    /**
     * Приоритет задачи.
     * Обязательное поле. Может быть HIGH, MEDIUM, LOW.
     */
    @NotNull(message = "Поле priority не должно быть пустым!")
    private TaskPriority priority; // Приоритет задачи (например, HIGH, MEDIUM, LOW). Обязательное поле.

    /**
     * Автор задачи.
     * Указывает, кто создал задачу. Обязательное поле.
     */
    private User author; // Автор задачи. Указывает, кто создал задачу. Обязательное поле.

    /**
     * Исполнитель задачи.
     * Указывает, кто отвечает за выполнение задачи. Поле необязательно.
     */
    private User executor; // Исполнитель задачи. Указывает, кто отвечает за выполнение задачи. Поле необязательно.

    /**
     * Список комментариев, связанных с задачей.
     * Управляется каскадированием и удалением "осиротевших" записей.
     */
    private List<CommentDTO> listOfComments; // Список комментариев, связанных с задачей. Связь управляется с помощью каскадирования и удаления "осиротевших" записей.
    //endRegion

    /**
     * Переопределение метода toString для представления задачи в виде строки.
     * Удобно для отладки и логирования.
     *
     * @return строковое представление задачи.
     */
    @Override
    public String toString()
    {
        return "TaskDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
