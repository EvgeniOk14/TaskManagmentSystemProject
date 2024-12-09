package com.example.taskmanagmentsystemproject.models.comment;

import com.example.taskmanagmentsystemproject.models.task.Task;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс Comment:
 *
 * Представляет сущность комментария в системе управления задачами.
 * Комментарий связан с конкретной задачей и пользователем.
 * Связь сущности с таблицами в базе данных:
 * - Сущность Comment связана с таблицей "comment_table" в базе данных PostgreSQL.
 * - Связь с таблицей "task_table" через внешний ключ "task_id".
 * - Связь с таблицей "table_users" через внешний ключ "user_id".
 *
 * Аннотации:
 * - @Data: библиотека Lombok для автоматической генерации геттеров, сеттеров и других методов.
 * - @AllArgsConstructor: Конструктор со всеми аргументами для создания объекта с полным набором данных.
 * - @NoArgsConstructor: Конструктор без аргументов для создания пустого объекта.
 * - @Entity: Обозначает класс как сущность JPA, обеспечивая автоматическое связывание с таблицей в базе данных.
 * - @Table: Задает имя таблицы в базе данных для данной сущности.
 * - @JsonIgnore: исключает из сериализации поле, над которым поставлена данная аннотация, чтобы избежать рекурсии
 */
@Data // Библиотека Lombok для генерации стандартных методов (геттеры, сеттеры, toString, equals, hashCode)
@AllArgsConstructor // Создает конструктор с полными аргументами
@NoArgsConstructor // Создает конструктор без аргументов
@Entity // Обозначает класс как сущность JPA, связывается с таблицей в базе данных
@Table(name = "comment_table") // Имя таблицы в базе данных, с которой будет связана сущность
public class Comment
{
    //region Fields
    /**
     * Уникальный идентификатор комментария.
     * Связан с первичным ключом таблицы "comment_table".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация идентификатора с использованием автоинкремента
    private Integer id; // Уникальный идентификатор комментария (первичный ключ в базе данных).

    /**
     * Текст комментария.
     * Поле "text" в таблице "comment_table". (Обязательное поле).
     */
    @Column(name = "text", nullable = false) // Название столбца в таблице и обязательность
    @NotEmpty(message = "Текст комментария не должен быть пустым.") // Проверка на непустое значение
    private String text; // Текст комментария. Обязательное поле.

    /**
     * Задача, к которой относится комментарий.
     * Связь с сущностью Task через внешний ключ "task_id".
     */
    @JsonIgnore // Эта аннотация исключит из сериализации поле с задачами, чтобы избежать рекурсии
    @ManyToOne(fetch = FetchType.EAGER) // Связь многие к одному с загрузкой данных при обращении
    @JoinColumn(name = "task_id", referencedColumnName = "id") // Название столбца и указание на внешний ключ
    private Task task; // Задача, к которой относится комментарий. Связь с таблицей "task_table".

    /**
     * Пользователь, оставивший комментарий.
     * Связь с сущностью User через внешний ключ "user_id".
     */
    @ManyToOne(fetch = FetchType.EAGER) // Связь многие к одному с загрузкой данных при обращении
    @JoinColumn(name = "user_id", nullable = false) // Название столбца и обязательность
    private User user; // Пользователь, оставивший комментарий.
    //endRegion

    //region Methods
    /**
     * Метод toString:
     * Переопределение метода toString для удобного отображения информации о комментарии.
     * Возвращает строковое представление комментария с его идентификатором и текстом.
     */
    @Override
    public String toString()
    {
        return "Comment{" +
                "id=" + id + // Идентификатор комментария
                ", text='" + text + '\'' + // Текст комментария
                '}';
    }
    //endRegion
}
