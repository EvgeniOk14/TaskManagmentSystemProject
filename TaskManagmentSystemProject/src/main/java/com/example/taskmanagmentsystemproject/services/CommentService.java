package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.dto.CommentDTO;
import com.example.taskmanagmentsystemproject.exceptions.customExeptions.*;
import com.example.taskmanagmentsystemproject.mapper.CommentMapper;
import com.example.taskmanagmentsystemproject.models.comment.Comment;
import com.example.taskmanagmentsystemproject.models.task.Task;
import com.example.taskmanagmentsystemproject.models.transformer.GenericTransformer;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.repository.CommentRepository;
import com.example.taskmanagmentsystemproject.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentService:
 *
 * Реализует бизнес-логику для работы с комментариями в приложении.
 * Отвечает за создание, получение, обновление и удаление комментариев, а также за их преобразование в DTO.
 *
 * Ключевые функции:
 * - Работа с объектами Comment и CommentDTO.
 * - Использование преобразователей и вспомогательных сервисов для обработки данных.
 * - Интеграция с базой данных через CommentRepository.
 * - Атомарность операций за счет аннотации @Transactional.
 *
 * Зависимости:
 * - CommentRepository: для доступа к данным комментариев.
 * - GenericTransformer: для преобразования между Comment и CommentDTO.
 * - CommentMapper: для трансформации (маппинга) полей между сущностью и DTO.
 * - UserHelperService: для получения данных о текущем пользователе.
 *
 * Методы:
 * - {@link #createNewComment(Integer, CommentDTO)}: Создает новый комментарий к найденной по id задаче, устанавливает комментарию текущего пользователя и сохраняет комментарий в базе данных и заново сохраняет задачу
 * - {@link #getAllCommentsByTaskId(Integer)}: Получает все комментарий по у задачи с заданным идентификатором.
 * - {@link #updateCommentById(Integer, CommentDTO)}: Обновляет комментарий по его идентификатору.
 * - {@link #deleteCommentById(Integer)}: Удаляет комментарий по его идентификатору.
 *
 * Исключения:
 * - {@link CommentNotSaveException}: кастомное исключение выбрасывается, если комментарий не может быть сохранен в базу данных.
 * - {@link CommentNotFoundException}: кастомное исключение выбрасывается, если комментарий не найден в базе данных.
 * - {@link CommentNotDeleteException}: кастомное исключение выбрасывается, если удаление комментария невозможно.
 * - {@link AuthentificationGetContextException}: кастомное исключение выбрасывается, если невозможно извлечь данные из контекста безопасности.
 * - {@link IllegalArgumentException}: стандартное системное исключение выбрасывается, если переданы некорректные аргументы.
 * - {@link FailTransformException}: кастомное исключение выбрасывается, если преобразование между объектами невозможно.
 *
 * Аннотации:
 * - @Service: указывает, что класс является сервисным компонентом в Spring.
 * - @Transactional: обеспечивает атомарность операций внутри методов.
 */

@Service
public class CommentService
{
    //region Fields
    private CommentRepository commentRepository;
    private GenericTransformer genericTransformer;
    private CommentMapper commentMapper;
    private UserHelperService userHelperService;

    private TaskRepository taskRepository;
    //endRegion

    //region Constructor
    public CommentService(CommentRepository commentRepository, GenericTransformer genericTransformer,
                          CommentMapper commentMapper, UserHelperService userHelperService,
                          TaskRepository taskRepository)
    {
        this.commentRepository = commentRepository;
        this.genericTransformer = genericTransformer;
        this.commentMapper = commentMapper;
        this.userHelperService = userHelperService;
        this.taskRepository = taskRepository;
    }
    //endRegion

    //region Methods
    /**
     * Метод: createNewComment:
     * Предназначен для: сохранения комментария в базе данных
     * @param commentDTO - переданный аргумент, объект класса CommentDTO (комментарий)
     * @param taskId - идентификационный номер задачи в базе данных, в таблицу "task_table", которой нужно добавить новый комментарий
     * @exception CommentNotSaveException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть сохранён в базе данных
     * @exception FailTransformException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @exception AuthentificationGetContextException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, невозможности извлечения данных из контекста безопасности
     * @exception TaskNotFoundException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможно найти задачу в базе данных
     * @return CommentDTO - возврат трансформированного комментария в формат CommentDTO
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public CommentDTO createNewComment(Integer taskId, CommentDTO commentDTO)
    {
        if (commentDTO == null) // проверяем если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент comment! Comment равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }

        if (taskId == null || taskId <= 0)  // проверяем корректность передачи аргумента taskId (должен быть положительный и больше нуля)
        {
            throw new IllegalArgumentException("Некорректный идентификатор задачи!"); // // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }

        try
        {
            Comment newComment = genericTransformer.transformFromDTOToEntity(commentDTO, commentMapper); // трансформируем комментарий в сущность Comment их объекта CommentDTO

            // Извлекаем текущий объект аутентификации из контекста безопасности (SecurityContext).
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // получаем объект Authentication, представляющий текущего аутентифицированного пользователя.
            String currentUserEmail =  authentication.getName(); // Получаем имя (или идентификатор) текущего пользователя из объекта Authentication.
            User currentUser = userHelperService.getUserByEmail(currentUserEmail); // получаем объект пользователя (User) по его email.

            newComment.setUser(currentUser); // устанавливаем комментарию пользователя, который оставил комментарий

            Task foundTask = taskRepository.getTaskById(taskId);

            newComment.setTask(foundTask);

            commentRepository.save(newComment); // комментарий в базе данных

            Task task =  taskRepository.getTaskById(taskId); // сохраняем

            List<Comment> listOfComments = task.getListOfComments(); // получаем список комментариев у полученной задачи

            if (listOfComments == null) // если список отсутствует, то:
            {
                listOfComments = new ArrayList<>(); // создаём новый список комментариев
                listOfComments.add(newComment); // добавляем новый комментарий в список к задаче
                task.setListOfComments(listOfComments); // добавляем этот список с комментариями к задаче
            }

            listOfComments.add(newComment); //  уже в существующий лист с комментариями к найденной задаче добавляем новый комментарий

            commentRepository.save(newComment); // сохранение комментария в базе данных, таблица "comment_table"
            taskRepository.save(task); // сохраняем задачу со списком комментариев в базе данных

            return genericTransformer.transformFromEntityToDTO(newComment, commentMapper); // возврат трансформированного комментария в формат CommentDTO
        }
        catch (CommentNotSaveException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть сохранён в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
        catch (AuthentificationGetContextException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, невозможности извлечения данных из контекста безопасности
        {
            throw ex;
        }
        catch (TaskNotFoundException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможно найти задачу в базе данных
        {
            throw ex;
        }
    }

    /**
     * Метод: getAllCommentsByTaskId:
     * Предназначен для: получения всех комментариев у задачи с id равной taskId, из базы данных таблицы "comment_table"
     * @param taskId - идентификационный номер задачи в базе данных, в таблице "task_table"
     * @exception CommentNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
     * @exception FailTransformException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return List<CommentDTO> - возврат списка трансформированных комментариев в формате CommentDTO
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public List<CommentDTO> getAllCommentsByTaskId(Integer taskId)
    {
        if (taskId == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент comment! Comment равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Task task = taskRepository.getTaskById(taskId); // получение задачи по её id
            List<Comment> commentList = task.getListOfComments(); // Получение списка комментариев у данной задачи
            if (commentList == null) // Проверяем, список комментариев равен нулю, если да, то:
            {
                return  Collections.emptyList(); // Возвращаем пустой список, если у задачи нет комментариев
            }

            return commentList.stream()
                    .map(comment -> genericTransformer.transformFromEntityToDTO(comment, commentMapper))
                    .collect(Collectors.toList()); // Возврат трансформированного списка трансформированных комментариев в формате CommentDTO


        }
        catch (CommentNotFoundException ex) // Кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // Кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод: updateCommentById:
     * Предназначен для: обновления комментария из базы данных таблицы "comment_table",  по id комментария
     * @param commentId - идентификационный номер комментария в базе данных
     * @param commentDTO - переданный аргумент, объект класса CommentDTO (комментарий)
     * @exception CommentNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
     * @exception FailTransformException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
     * @exception AuthentificationGetContextException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, невозможности извлечения данных из контекста безопасности
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return CommentDTO - возврат трансформированного комментария в формат CommentDTO
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public CommentDTO updateCommentById(Integer commentId, CommentDTO commentDTO)
    {
        if (commentDTO == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент comment! Comment равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Comment updatedComment = commentRepository.getCommentById(commentId); // получение комментария из базе данных, таблицы "comment_table" по его id

            // Извлекаем текущий объект аутентификации из контекста безопасности (SecurityContext).
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // получаем объект Authentication, представляющий текущего аутентифицированного пользователя.
            String currentUserEmail = authentication.getName(); // Получаем имя (или идентификатор) текущего пользователя из объекта Authentication.
            User currentUser = userHelperService.getUserByEmail(currentUserEmail); // получаем объект пользователя (User) по его email.

            // обновляем комментарий:
            updatedComment.setText(commentDTO.getText()); // устанавливаем текст комментария
            updatedComment.setTask(commentDTO.getTask()); // устанавливаем комментарий к задаче
            updatedComment.setUser(currentUser); // устанавливаем пользователя обновившего комментарий

            commentRepository.save(updatedComment); // Сохраняем обновлённый объект комментарий в базе данных

            return genericTransformer.transformFromEntityToDTO(updatedComment, commentMapper); // возврат трансформированного комментария в формат CommentDTO

            }

            catch (CommentNotFoundException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
            {
                throw ex;
            }
            catch (FailTransformException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
            {
                throw ex;
            }
            catch (AuthentificationGetContextException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, невозможности извлечения данных из контекста безопасности
            {
                throw ex;
            }
        }

        /**
         * Метод: deleteCommentById:
         * Предназначен для: удаления комментария из базы данных таблицы "comment_table", по id комментария
         * @param commentId - идентификационный номер комментария в базе данных, который нужно удалить
         * @exception CommentNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
         * @exception FailTransformException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
         * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
         * @exception CommentNotDeleteException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможности удаления объекта комментарий из базы данных
         * @return CommentDTO - возврат трансформированного комментария в формат CommentDTO
         * **/

        public CommentDTO deleteCommentById(Integer commentId)
        {
            if (commentId == null) // если переданный аргумент равен null, то:
            {
                throw new IllegalArgumentException("Некорректный аргумент commentId! CommentId равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
            }
            try
            {
                Comment deletedComment = commentRepository.getCommentById(commentId); // получение комментария из базе данных, таблицы "comment_table" по его id
                commentRepository.delete(deletedComment);

                return genericTransformer.transformFromEntityToDTO(deletedComment, commentMapper); // возврат трансформированного комментария в формат CommentDTO
            }
            catch (CommentNotFoundException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
            {
                throw ex;
            }
            catch (FailTransformException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
            {
                throw ex;
            }
            catch (CommentNotDeleteException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможности удаления объекта комментарий из базы данных
            {
                throw  ex;
            }
        }

        /**
         * Метод getCommentById:
         * Получает комментарий по его id
         * @param commentId - идентификационный номер комментария в базе данных, который нужно получить по его id
         * @exception CommentNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
         * @exception FailTransformException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
         * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
         * @return CommentDTO - объект типа CommentDTO (комментарий)
         * **/
        @Transactional // Обеспечиваем атомарность всех операций в методе.
        public CommentDTO getCommentById(Integer commentId)
        {
            if (commentId == null) // если переданный аргумент равен null, то:
            {
                throw new IllegalArgumentException("Некорректный аргумент commentId! CommentId равен null."); // выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
            }
            try
            {
               Comment foundComment =  commentRepository.getCommentById(commentId); // нахождение коментария по его id  в базе данных таблице "comment_table"
               return genericTransformer.transformFromEntityToDTO(foundComment, commentMapper); // Возвращает трансформированный комментарий CommentDTO
            }
            catch (CommentNotFoundException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если комментарий не может быть найден в базе данных
            {
                throw  ex;
            }
            catch (FailTransformException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
            {
                throw  ex;
            }
        }

    //endRegion
}
