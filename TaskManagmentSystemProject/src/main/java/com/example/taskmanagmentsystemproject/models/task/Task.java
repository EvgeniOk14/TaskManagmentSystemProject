package com.example.taskmanagmentsystemproject.models.task;

import com.example.taskmanagmentsystemproject.models.comment.Comment;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Класс Task:
 *
 * Представляет сущность задачи в системе управления задачами.
 * Задача содержит такие атрибуты, как: заголовок, описание, статус, приоритет, автор, исполнитель,
 * список связанных комментариев.
 * Связь сущности с таблицами в базе данных:
 * - Сущность Task связана с таблицей "task_table" в базе данных PostgreSQL.
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
@Table(name = "task_table") // Имя таблицы в базе данных, с которой будет связана сущность
public class Task
{
    //region Fields
    /**
     * Уникальный идентификатор задачи.
     * Связан с первичным ключом таблицы "task_table".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация идентификатора с использованием автоинкремента
    private Integer id; // Уникальный идентификатор задачи (первичный ключ).

    /**
     * Заголовок задачи.
     * Поле "task_title" в таблице "task_table". (Обязательное поле).
     */
    @Column(name = "task_title", nullable = false) // Название столбца в таблице и указание, что поле не может быть пустым
    @NotEmpty(message = "Поле title не должно быть пустым!") // Проверка на непустое значение
    private String title; // Заголовок задачи. Обязательное поле.

    /**
     * Подробное описание задачи.
     * Поле "task_description" в таблице "task_table". (Необязательное поле).
     */
    @Column(name = "task_description") // Название столбца в таблице
    private String description; // Описание задачи (необязательное поле).

    /**
     * Текущий статус задачи.
     * Поле "task_status" в таблице "task_table". (Обязательное поле).
     * Статус задачи может быть: PENDING, IN_PROGRESS, COMPLETED.
     */
    @Enumerated(EnumType.STRING) // Хранение перечисления в виде строки
    @NotNull(message = "Поле status не должно быть пустым!") // Проверка на наличие значения
    @Column(name = "task_status", nullable = false) // Название столбца и обязательность
    private TaskStatus status; // Статус задачи (например, PENDING, IN_PROGRESS, COMPLETED).

    /**
     * Приоритет задачи.
     * Поле "task_priority" в таблице "task_table". (Обязательное поле).
     * Приоритет задачи может быть: HIGH, MEDIUM, LOW.
     */
    @Enumerated(EnumType.STRING) // Хранение перечисления в виде строки
    @NotNull(message = "Поле priority не должно быть пустым!") // Проверка на наличие значения
    @Column(name = "task_priority", nullable = false) // Название столбца и обязательность
    private TaskPriority priority; // Приоритет задачи (например, HIGH, MEDIUM, LOW).

    /**
     * Автор задачи.
     * Связь с сущностью User через внешний ключ "author_id".
     * Автор задачи обязателен.
     */
    @ManyToOne(fetch = FetchType.EAGER) // Связь многие к одному с загрузкой данных при обращении
    @JoinColumn(name = "author_id", nullable = false) // Название столбца в таблице и обязательность
    private User author; // Автор задачи.

    /**
     * Исполнитель задачи.
     * Связь с сущностью User через внешний ключ "executor_id".
     * Исполнитель задачи не обязателен.
     */
    @ManyToOne(fetch = FetchType.EAGER) // Связь многие к одному с загрузкой данных при обращении
    @JoinColumn(name = "executor_id") // Название столбца в таблице
    private User executor; // Исполнитель задачи.

    /**
     * Список комментариев, связанных с задачей.
     * Связь с сущностью Comment через внешний ключ "task_id".
     * Комментарии будут автоматически удаляться при удалении задачи (каскадирование).
     */
    @JsonIgnore // Эта аннотация исключит из сериализации поле с задачами, чтобы избежать рекурсии
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Связь один ко многим с каскадированием
    private List<Comment> listOfComments; // Список комментариев, связанных с задачей.
    //endRegion

    //region Methods
    /**
     * Метод toString:
     * Переопределение метода toString для удобного отображения информации о задаче.
     * Возвращает строковое представление задачи с её идентификатором и заголовком.
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id + // Идентификатор задачи
                ", name='" + title + '\'' + // Заголовок задачи
                '}';
    }
    //endRegion
}
