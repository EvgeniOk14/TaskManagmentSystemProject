package com.example.taskmanagmentsystemproject.repository;

import com.example.taskmanagmentsystemproject.models.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс CommentRepository:
 *
 * Отвечает за доступ к данным комментариев в базе данных. Расширяет интерфейс JpaRepository, что позволяет использовать стандартные методы CRUD.
 * Включает дополнительные методы для поиска комментариев.
 *
 * Основные функции:
 * - Использует аннотацию @Repository для интеграции с Spring.
 * - Реализует поиск комментариев по идентификатору.
 *
 * Методы:
 * - {@link #getCommentById(Integer)}: Получает комментарий по его идентификатору.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>
{
    //region Methods
    /**
     * Метод getCommentById:
     * Получает комментарий по его идентификатору.
     *
     * @param id Идентификатор комментария.
     * @return объект Comment, представляющий найденный комментарий.
     * @exception IllegalArgumentException выбрасывается, если передан некорректный ID.
     */
    Comment getCommentById(Integer id);
    //endRegion
}
