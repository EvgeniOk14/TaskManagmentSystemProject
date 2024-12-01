package com.example.taskmanagmentsystemproject.models.user;

import com.example.taskmanagmentsystemproject.models.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Класс User:
 *
 * Представляет сущность пользователя в системе управления задачами.
 * Пользователь включает в себя такие атрибуты, как:
 * - Уникальный идентификатор (id).
 * - Электронная почта (email).
 * - Пароль (password).
 * - Роль пользователя (userRole), определяющая его права.
 * - Список задач, созданных пользователем (createdTasks).
 * - Список задач, назначенных пользователю (assignedTasks).
 *
 * Связь с базой данных:
 * - Таблица "table_users" хранит данные пользователей.
 * - Связь с таблицей "task_table" осуществляется через атрибуты "author_id" и "executor_id".
 *
 * Аннотации:
 * - @Component: помечает класс как компонент Spring для внедрения зависимостей.
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 * - @Entity: обозначает класс как сущность JPA.
 * - @Table: задает имя таблицы в базе данных.
 * - @JsonIgnore: исключает из сериализации поле, над которым поставлена данная аннотация, чтобы избежать рекурсии
 *
 * Поля:
 * - {@link #id}: уникальный идентификатор пользователя.
 * - {@link #email}: адрес электронной почты пользователя.
 * - {@link #password}: пароль пользователя.
 * - {@link #userRole}: роль пользователя.
 * - {@link #createdTasks}: список задач, созданных пользователем.
 * - {@link #assignedTasks}: список задач, назначенных пользователю.
 */
@Component // помечает класс как компонент Spring для внедрения зависимостей
@Data  // библиотека Lombok
@AllArgsConstructor // Конструктор со всеми аргументами
@NoArgsConstructor  // конструктор без аргументов
@Entity // для создание у сущности автоматической связи с таблицей в базе данных
@Table(name = "table_users")  // название создаваемой таблицы
public class User
{
    //region Fields
    /**
     * Уникальный идентификатор пользователя.
     * Связан с первичным ключом таблицы "table_users".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // номер пользователя в базе данных

    /**
     * Электронная почта пользователя.
     * Поле "user_email" в таблице "table_users". (Результатом будет: создано поле "user_email" в таблице "table_users")
     * Уникальное и обязательное поле.
     */
    @Column(name = "user_email", nullable = false, unique = true)
    @NotEmpty(message = "Email не должен быть пустым.")
    @Email(message = "Введите корректный email.")
    private String email; // // Поле для хранения email пользователя.

    /**
     * Пароль пользователя.
     * Поле "user_password" в таблице "table_users". (Результатом будет: создано поле "user_password" в таблице "table_users")
     * Обязательное поле.
     */
    @Column(name = "user_password", nullable = false)
    @NotEmpty(message = "Пароль не должен быть пустым.")
    @Size(min = 5, message = "Пароль должен содержать не менее 5 символов.")
    private String password; // // Поле для хранения пароля пользователя.

    /**
     * Роль пользователя.
     * Поле "user_role" в таблице "table_users". (Результатом будет: создано поле "user_role" в таблице "table_users")
     * Обязательное поле.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    @NotNull(message = "Роль не должна быть пустой!")
    private UserRole userRole; // набор ролей для распределения пользователям в Spring Security: (USER_ROLE, ADMIN_ROLE)

    /**
     * Список задач, созданных пользователем.
     * Поле "author_id" в таблице "task_table", связанное с "id" пользователя. (Результатом будет: создано поле "author_id" в таблице "task_table", которое связано с id пользователя из таблицы "table_users")
     */
    @JsonIgnore // Эта аннотация исключит из сериализации поле с задачами, чтобы избежать рекурсии
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Task> createdTasks; // Список задач, созданных пользователем.

    /**
     * Список задач, назначенных пользователю.
     * Поле "executor_id" в таблице "task_table", связанное с "id" пользователя. (Результатом будет: создано поле "executor_id" в таблице "task_table", которое связано с id пользователя из таблицы "table_users")
     */
    @OneToMany(mappedBy = "executor", cascade = CascadeType.ALL)
    private List<Task> assignedTasks; // Список задач, назначенных пользователю.
    //endRegion
}
