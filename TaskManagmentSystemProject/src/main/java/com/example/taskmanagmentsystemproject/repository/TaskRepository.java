package com.example.taskmanagmentsystemproject.repository;

import com.example.taskmanagmentsystemproject.models.task.Task;
import com.example.taskmanagmentsystemproject.models.task.TaskPriority;
import com.example.taskmanagmentsystemproject.models.task.TaskStatus;
import com.example.taskmanagmentsystemproject.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Интерфейс TaskRepository:
 *
 * Отвечает за доступ к данным задач в базе данных. Расширяет интерфейс JpaRepository, что позволяет использовать стандартные методы CRUD.
 * Включает дополнительные методы для фильтрации, поиска и удаления задач.
 *
 * Основные функции:
 * - Использует аннотацию @Repository для интеграции с Spring.
 * - Включает методы для поиска задач по различным критериям (ID, автор, исполнитель).
 * - Реализует кастомные запросы с помощью @Query.
 *
 * Методы:
 * - {@link #getTaskById(Integer)}: Получает задачу по её идентификатору.
 * - {@link #findTasksByFilters(String, String, TaskStatus, TaskPriority, Pageable)}: Выполняет поиск задач с фильтрацией по нескольким критериям.
 * - {@link #findByAuthor(User)}: Получает список задач, созданных определенным автором.
 * - {@link #findByExecutor(User)}: Получает список задач, назначенных определенному исполнителю.
 * - {@link #deleteById(Integer)}: Удаляет задачу по её идентификатору.
 * - {@link #findByAuthorEmailAndExecutorEmail(String, String)}: Находит задачи, где пользователь является и автором, и исполнителем.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>
{
    //region Methods
    /**
     * Метод getTaskById:
     * Получает задачу по её идентификатору.
     *
     * @param id Идентификатор задачи.
     * @return объект Task, представляющий найденную задачу.
     * @exception IllegalArgumentException выбрасывается, если передан некорректный ID.
     */
    Task getTaskById(Integer id);

    /**
     * Метод findTasksByFilters:
     * Выполняет поиск задач с использованием фильтров.
     *
     * @param authorEmail    Email автора задачи (может быть null).
     * @param executorEmail  Email исполнителя задачи (может быть null).
     * @param status         Статус задачи (может быть null).
     * @param priority       Приоритет задачи (может быть null).
     * @param pageable       Объект пагинации для результатов.
     * @return Страница задач, соответствующих заданным фильтрам.
     */
    @Query("SELECT t FROM Task t " +
            "LEFT JOIN FETCH t.listOfComments c " +
            "LEFT JOIN t.author a " +
            "LEFT JOIN t.executor e " +
            "WHERE (:authorEmail IS NULL OR a.email = :authorEmail) " +
            "AND (:executorEmail IS NULL OR e.email = :executorEmail) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority)")
    Page<Task> findTasksByFilters(
            @Param("authorEmail") String authorEmail,
            @Param("executorEmail") String executorEmail,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            Pageable pageable
    );


    /**
     * Метод findByAuthor:
     * Получает список задач, созданных указанным автором.
     *
     * @param author Объект User, представляющий автора задач.
     * @return Список задач, созданных автором.
     */
    List<Task> findByAuthor(User author);

    /**
     * Метод findByExecutor:
     * Получает список задач, назначенных указанному исполнителю.
     *
     * @param author Объект User, представляющий исполнителя задач.
     * @return Список задач, назначенных исполнителю.
     */
    List<Task> findByExecutor(User author);

    /**
     * Метод deleteById:
     * Удаляет задачу по её идентификатору.
     *
     * @param id Идентификатор задачи.
     */
    void deleteById(Integer id);

    /**
     * Метод findByAuthorEmailAndExecutorEmail:
     * Находит задачи, где пользователь является и автором, и исполнителем.
     *
     * @param authorEmail    Email автора задачи.
     * @param executorEmail  Email исполнителя задачи.
     * @return Список задач, где пользователь одновременно является автором и исполнителем.
     */
    List<Task> findByAuthorEmailAndExecutorEmail(String authorEmail, String executorEmail);
    //endRegion
}
