package com.example.taskmanagmentsystemproject.controllers;

import com.example.taskmanagmentsystemproject.dto.CommentDTO;
import com.example.taskmanagmentsystemproject.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Класс CommentsController:
 *
 * Реализует REST API для работы с комментариями в системе. Предоставляет endpoints для создания, обновления, получения и удаления комментариев,
 * а также для получения всех комментариев, связанных с конкретной задачей.
 *
 * Основные функции:
 * - Создание нового комментария для конкретной задачи.
 * - Получение списка всех комментариев для конкретной задачи.
 * - Обновление комментария по его идентификатору.
 * - Удаление комментария по его идентификатору.
 * - Получение конкретного комментария по его идентификатору.
 *
 * Зависимости:
 * - CommentService: сервис для выполнения бизнес-логики, связанной с комментариями.
 *
 * Методы:
 * - {@link #addNewComment(Integer, CommentDTO)}: Создает новый комментарий для задачи.
 * - {@link #getCommentsByTask(Integer)}: Получает все комментарии для задачи по её идентификатору.
 * - {@link #updateCommentById(Integer, CommentDTO)}: Обновляет комментарий по его идентификатору.
 * - {@link #deleteCommentById(Integer)}: Удаляет комментарий по его идентификатору.
 * - {@link #getCommentById(Integer)}: Получает комментарий по его идентификатору.
 *
 * Конечные точки:
 * - {@code POST /api/comments/{taskId}/createComment}: Создает новый комментарий для задачи с указанным идентификатором.
 * - {@code GET /api/comments/{taskId}/getAllComments}: Получает все комментарии для задачи с указанным идентификатором.
 * - {@code POST /api/comments/{commentId}/updateCommentById}: Обновляет комментарий с указанным идентификатором.
 * - {@code POST /api/comments/{commentId}/delete}: Удаляет комментарий с указанным идентификатором.
 * - {@code GET /api/comments/{commentId}/getComment}: Получает комментарий с указанным идентификатором.
 *
 * Архитектурные особенности:
 * - Контроллер взаимодействует с бизнес-логикой через CommentService.
 * - Использует аннотации Spring для маршрутизации запросов.
 * - Возвращает ответы клиенту в формате ResponseEntity.
 * - Работает с данными, представленными в формате CommentDTO.
 *
 * HTTP-методы:
 * - POST: для создания, обновления и удаления комментариев.
 * - GET: для получения комментариев.
 *
 * Исключения:
 * - Все исключения, возникающие при выполнении запросов, обрабатываются на уровне сервиса (CommentService).
 *
 * Аннотации:
 * - @RestController: указывает, что класс является REST контроллером.
 * - @RequestMapping("/api/comments"): задает базовый путь для всех endpoints в этом контроллере.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentsController
{
    //region Fields
    private CommentService commentService;
    //endRegion

    //region Constructor
    public CommentsController(CommentService commentService)
    {
        this.commentService = commentService;
    }
    //endRegion

    //region Methods
    /**
     * Метод addNewComment:
     * Создаёт новый комментарий и добавляет его в список комментариев к конкретной задаче
     * (создание нового комментария в списке комментариев у конкретной задачи, найденной по её id)
     * @param taskId - идентификационный номер задачи, которой необходимо поставить комментарий
     * @param commentDTO - объект типа CommentDTO, т.е. трансформированный для передачи комментарий к задаче
     * @return ResponseEntity<String> - положительный ответ пользователю с новым созданным комментарием и со статусом OK
     * **/
    @PostMapping("/{taskId}/createComment")
    public ResponseEntity<String> addNewComment(@PathVariable Integer taskId, @RequestBody @Valid CommentDTO commentDTO)
    {
         CommentDTO newCommentDTO = commentService.createNewComment(taskId, commentDTO); // создаём через сервис новый комментарий
        return ResponseEntity.status(HttpStatus.OK).body("Комментарий: " + newCommentDTO + " успешно добавлен к задаче с номером id равным: " + taskId);
    }

    /**
     * Метод getCommentsByTask:
     * получает список всех комментариев к конкретной задачи (по id задачи)
     * @param taskId - идентификационный номер задачи, у которой необходимо получить все комментарии
     * @return ResponseEntity<List<CommentDTO>> - положительный ответ пользователю, со статусом OK и списком всех комментариев
     * **/
    @GetMapping("/{taskId}/getAllComments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTask(@PathVariable Integer taskId)
    {
        List<CommentDTO> comments =  commentService.getAllCommentsByTaskId(taskId); // получаем через сервис список комментариев
        return ResponseEntity.ok(comments);
    }

    /**
     * Метод updateCommentById:
     * обновляет конкретный комментарий по его id
     * @param commentId - идентификационный номер комментария в базе данных, который нужно обновить
     * @param commentDTO - переданный трансформированный объект CommentDTO (комментарий), данные которого необходимо обновить в базе данных у комментария с id равным переданному в запросе taskId
     * @return ResponseEntity<String> - положительный ответ пользователю, со статусом OK и обновлённым комментарием
     * **/
    @PostMapping("/{commentId}/updateCommentById")
    public ResponseEntity<String> updateCommentById(@PathVariable Integer commentId,  @RequestBody CommentDTO commentDTO)
    {
        CommentDTO updatedCommentDTO = commentService.updateCommentById(commentId, commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Комментарий c id равным " + commentId  + " успешно обновлён в базе данных! Обновлённый комментарий: " + updatedCommentDTO);
    }

    /**
     * Метод deleteCommentById:
     * удаляет конкретный комментарий по его id
     * @param commentId - идентификационный номер комментария в базе данных, который нужно удалить
     * @return ResponseEntity<String> - положительный ответ пользователю, со статусом OK и удалённым комментарием
     * **/
    @PostMapping("/{commentId}/delete")
    public ResponseEntity<String> deleteCommentById(@PathVariable Integer commentId)
    {
        CommentDTO deletedComment = commentService.deleteCommentById(commentId); // удаляем через сервис комментарий
        return ResponseEntity.status(HttpStatus.OK).body("Комментарий " + deletedComment + " успешно удалён из базы данных! ");
    }

    /**
     * Метод getCommentById:
     * получает конкретный комментарий по его id
     * @param commentId - идентификационный номер комментария, который необходимо получить
     * @return ResponseEntity<String> - положительный ответ пользователю, со статусом OK и полученным комментарием
     * **/
    @GetMapping("/{commentId}/getComment")
    public ResponseEntity<String> getCommentById(@PathVariable Integer commentId)
    {
        CommentDTO commentDTO = commentService.getCommentById(commentId); // получаем через сервис комментарий
        return ResponseEntity.status(HttpStatus.OK).body("по id равным " + commentId + " найден комментарий: " + commentDTO);
    }
    //endRegion
}
