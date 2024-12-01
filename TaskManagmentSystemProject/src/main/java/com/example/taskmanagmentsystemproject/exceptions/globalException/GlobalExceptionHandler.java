package com.example.taskmanagmentsystemproject.exceptions.globalException;

import com.example.taskmanagmentsystemproject.exceptions.customExeptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Класс GlobalExceptionHandler:
 *
 * Глобальный обработчик исключений, предназначенный для обработки различных исключений, возникающих
 * в приложении. Реализует централизованное управление ошибками, возвращая соответствующий HTTP-статус
 * и сообщение об ошибке в ответ клиенту.
 *
 * Основные особенности:
 * - Логирование ошибок с использованием {@link Logger}.
 * - Возвращает корректные HTTP-статусы, соответствующие типу возникшей ошибки.
 * - Упрощает отладку и обработку ошибок на стороне клиента.
 *
 * Аннотация:
 * - {@link ControllerAdvice}: Указывает, что этот класс обрабатывает исключения на глобальном уровне
 *   для всех контроллеров приложения.
 *
 * Методы:
 * - {@link #handleBookNotFoundException(PasswordEmptyException)}:
 *   Обрабатывает исключение {@link PasswordEmptyException}, возвращая статус BAD_REQUEST.
 *
 * - {@link #handlePasswordSizeException(PasswordSizeException)}:
 *   Обрабатывает исключение {@link PasswordSizeException}, возвращая статус BAD_REQUEST.
 *
 * - {@link #handleFailTransformExeption(FailTransformException)}:
 *   Обрабатывает исключение {@link FailTransformException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerUserNotSaveException(UserNotSaveException)}:
 *   Обрабатывает исключение {@link UserNotSaveException}, возвращая статус BAD_REQUEST.
 *
 * - {@link #handlerUserNotFoundException(UserNotFoundException)}:
 *   Обрабатывает исключение {@link UserNotFoundException}, возвращая статус NOT_FOUND.
 *
 * - {@link #handlerFailEncodePasswordException(FailEncodePasswordException)}:
 *   Обрабатывает исключение {@link FailEncodePasswordException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerTaskNotSaveExeption(TaskNotSaveExсeption)}:
 *   Обрабатывает исключение {@link TaskNotSaveExсeption}, возвращая статус BAD_REQUEST.
 *
 * - {@link #handlerEmailIsNullExeption(EmailIsNullExeption)}:
 *   Обрабатывает исключение {@link EmailIsNullExeption}, возвращая статус BAD_REQUEST.
 *
 * - {@link #handlerTaskNotFoundException(TaskNotFoundException)}:
 *   Обрабатывает исключение {@link TaskNotFoundException}, возвращая статус NOT_FOUND.
 *
 * - {@link #handlerTaskNotDeleteException(TaskNotDeleteException)}:
 *   Обрабатывает исключение {@link TaskNotDeleteException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerTaskNotFoundArgumentException(NotFoundArgumentException)}:
 *   Обрабатывает исключение {@link NotFoundArgumentException}, возвращая статус NOT_FOUND.
 *
 * - {@link #handlerNotMappingException(NotMappingException)}:
 *   Обрабатывает исключение {@link NotMappingException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerCommentNotSaveException(CommentNotSaveException)}:
 *   Обрабатывает исключение {@link CommentNotSaveException}, возвращая статус BAD_REQUEST.
 *
 * - {@link #handlerCommentNotFoundException(CommentNotFoundException)}:
 *   Обрабатывает исключение {@link CommentNotFoundException}, возвращая статус NOT_FOUND.
 *
 * - {@link #handlerAuthentificationGatContextException(AuthentificationGetContextException)}:
 *   Обрабатывает исключение {@link AuthentificationGetContextException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerCommentNotDeleteException(CommentNotDeleteException)}:
 *   Обрабатывает исключение {@link CommentNotDeleteException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerTokenNotDeleteException(TokenNotDeleteException)}:
 *   Обрабатывает исключение {@link TokenNotDeleteException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerTokenNotSaveException(TokenNotSaveException)}:
 *   Обрабатывает исключение {@link TokenNotSaveException}, возвращая статус CONFLICT.
 *
 * - {@link #handlerTokenNotGenerateException(TokenNotGenerateException)}:
 *   Обрабатывает исключение {@link TokenNotGenerateException}, возвращая статус CONFLICT.
 */
@ControllerAdvice // Аннотация указывает, что класс является обработчиком исключений
public class GlobalExceptionHandler
{

    //region Fields
    private static final Logger logger =  LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //endRegion

    //region Methods
    /**
     * Обрабатывает исключение PasswordEmptyException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST
     */
    @ExceptionHandler(PasswordEmptyException.class) // Аннотация указывает, что этот метод обрабатывает PasswordEmptyException
    public ResponseEntity<String> handleBookNotFoundException(PasswordEmptyException ex)
    {
        logger.info("Ошибка: Пустой пароль!: {}" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка - пустой пароль: " + ex.getMessage()); // Возвращает статус BAD_REQUEST с сообщением
    }

    /**
     * Обрабатывает исключение PasswordSizeException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST
     */
    @ExceptionHandler(PasswordSizeException.class)
    public ResponseEntity<String> handlePasswordSizeException(PasswordSizeException ex)
    {
        logger.info("Ошибка: Длина пароля меньше пяти символов! {} " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: Длина пароля меньше пяти символов!" + ex.getMessage());
    }

    /**
     * Обрабатывает исключение PasswordSizeException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением об ошибке и статусом CONFLICT
     */
    @ExceptionHandler(FailTransformException.class)
    public ResponseEntity<String> handleFailTransformExeption(FailTransformException ex)
    {
        logger.info(("Ошибка: объект не может быть трансформирован в другой объект! "));

        // Возвращает статус CONFLICT с сообщением о том, что объект не может быть трансформирован в другой объект
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ошибка: объект не может быть трансформирован в другой объект! " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение UserNotSaveException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что сохранение пользователя не осуществлено и статусом BAD_REQUEST
     */
    @ExceptionHandler(UserNotSaveException.class)
    public ResponseEntity<String> handlerUserNotSaveException(UserNotSaveException ex)
    {
        logger.info(("Ошибка: объект не может быть сохранён в базе данных! ")); // Логирует сообщение об ошибке

        // Возвращает статус BAD_REQUEST с сообщением о том, что сохранение в базу данных пользователя не осуществлено
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Сохранение в базу данных пользователя User не осуществлено: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение UserNotFoundException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что пользователь не найден в базе данных и статусом NOT_FOUND
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handlerUserNotFoundException(UserNotFoundException ex)
    {
        logger.info(("Ошибка: пользователь с таким id не найден в базе данных! ")); // Логирует сообщение об ошибке

        // Возвращает статус NOT_FOUND с сообщением о том, что пользователь не найден в базе данных
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Сохранение в базу данных пользователя User не осуществлено: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение FailEncodePasswordException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что кодировка пароля не может быть осуществлена и статусом CONFLICT
     */
    @ExceptionHandler(FailEncodePasswordException.class)
    public ResponseEntity<String> handlerFailEncodePasswordException(FailEncodePasswordException ex)
    {
        logger.info("Ошибка: кодировка пароля не может быть осуществлена! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, что кодировка пароля не может быть осуществлена
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Кодировка пароля не может быть осуществлена: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение TaskNotSaveExсeption.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что задача не может быть сохранена в базе данных и статусом BAD_REQUEST
     */
    @ExceptionHandler(TaskNotSaveExсeption.class)
    public ResponseEntity<String> handlerTaskNotSaveExeption(TaskNotSaveExсeption ex)
    {
        logger.info("Ошибка: задача не может быть сохранена в базе данных! "); //  Логирует сообщение об ошибке
        // Возвращает статус BAD_REQUEST с сообщением о том, что задача не может быть сохранена в базе данных.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задача не может быть сохранена в базе данных: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение EmailIsNullExeption.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что email пользователя отсутствует и статусом BAD_REQUEST
     */
    @ExceptionHandler(EmailIsNullExeption.class)
    public ResponseEntity<String> handlerEmailIsNullExeption(EmailIsNullExeption ex)
    {
        logger.info("Ошибка: email пользователя отсутствует! "); //  Логирует сообщение об ошибке
        // Возвращает статус BAD_REQUEST с сообщением о том, что email пользователя отсутствует!.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email пользователя отсутствует: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение TaskNotFoundException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что задача с таким id не найдена в базе данных и статусом NOT_FOUND
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handlerTaskNotFoundException(TaskNotFoundException ex)
    {
        logger.info("Ошибка: задача с таким id не найдена в базе данных! "); // Логирует сообщение об ошибке

        // Возвращает статус NOT_FOUND с сообщением о том, что пользователь не найден в базе данных
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("задача с таким id не найдена в базе данных: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение TaskNotDeleteException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что невозможно удаление задачи по её id из базы данных и статусом CONFLICT
     */
    @ExceptionHandler(TaskNotDeleteException.class)
    public ResponseEntity<String> handlerTaskNotDeleteException(TaskNotDeleteException ex)
    {
        logger.info("Ошибка: невозможно удалить задачу по её id! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, что пользователь не найден в базе данных
        return ResponseEntity.status(HttpStatus.CONFLICT).body("невозможно удаление задачи по её id из базы данных: " + ex.getMessage());
    }


    /**
     * Обрабатывает исключение NotFoundArgumentException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что запрашиваемый аргумент у данного объекта не найден и статусом NOT_FOUND
     */
    @ExceptionHandler(NotFoundArgumentException.class)
    public ResponseEntity<String> handlerTaskNotFoundArgumentException(NotFoundArgumentException ex)
    {
        logger.info("Ошибка: запрашиваемый аргумент у данного объекта не найден! "); // Логирует сообщение об ошибке

        // Возвращает статус NOT_FOUND с сообщением о том, что какой-либо аргумент класса Task не найден!
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("запрашиваемый аргумент у данного объекта не найден!: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение NotMappingException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, проблемы маппинга одной сущности в другую и статусом CONFLICT
     */
    @ExceptionHandler(NotMappingException.class)
    public ResponseEntity<String> handlerNotMappingException(NotMappingException ex)
    {
        logger.info("Ошибка: проблемы маппинга одной сущности в другую! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, проблемы маппинга одной сущности в другую!
        return ResponseEntity.status(HttpStatus.CONFLICT).body("проблемы маппинга одной сущности в другую!: " + ex.getMessage());
    }


    /**
     * Обрабатывает исключение CommentNotSaveException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что комментарий не может быть сохранён в базе данных и статусом NOT_FOUND
     */
    @ExceptionHandler(CommentNotSaveException.class)
    public ResponseEntity<String> handlerCommentNotSaveException(CommentNotSaveException ex)
    {
        logger.info("Ошибка: комментарий не может быть сохранён в базе данных! "); //  Логирует сообщение об ошибке
        // Возвращает статус BAD_REQUEST с сообщением о том, что комментарий не может быть сохранён в базе данных.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Комментарий не может быть сохранён в базе данных: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение CommentNotFoundException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что комментарий не может быть найден в базе данных и статусом NOT_FOUND
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handlerCommentNotFoundException(CommentNotFoundException ex)
    {
        logger.info("Ошибка: задача с таким id не найдена в базе данных! "); // Логирует сообщение об ошибке

        // Возвращает статус NOT_FOUND с сообщением о том, что комментарий не может быть найден в базе данных
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("комментарий не может быть найден в базе данных: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение AuthentificationGatContextException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, проблемы извлечения данных из контекста безопасности и статусом CONFLICT
     */
    @ExceptionHandler(AuthentificationGetContextException.class)
    public ResponseEntity<String> handlerAuthentificationGatContextException(AuthentificationGetContextException ex)
    {
        logger.info("Ошибка: проблемы извлечения данных из контекста безопасности! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, проблемы извлечения данных из контекста безопасности!
        return ResponseEntity.status(HttpStatus.CONFLICT).body("проблемы извлечения данных из контекста безопасности! "  + ex.getMessage());
    }

    /**
     * Обрабатывает исключение CommentNotDeleteException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что невозможно удаление комментария по его id из базы данных и статусом CONFLICT
     */
    @ExceptionHandler(CommentNotDeleteException.class)
    public ResponseEntity<String> handlerCommentNotDeleteException(CommentNotDeleteException ex)
    {
        logger.info("Ошибка: невозможно удалить комментарий по его id! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, что пользователь не найден в базе данных
        return ResponseEntity.status(HttpStatus.CONFLICT).body("невозможно удалить комментарий по его id из базы данных: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение TokenNotDeleteException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что невозможно удаление токена и статусом CONFLICT
     */
    @ExceptionHandler(TokenNotDeleteException.class)
    public ResponseEntity<String> handlerTokenNotDeleteException(TokenNotDeleteException ex)
    {
        logger.info("Ошибка: невозможно удалить токен! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, невозможно удалить токен
        return ResponseEntity.status(HttpStatus.CONFLICT).body("невозможно удаление токена: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение TokenNotSaveException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что невозможно сохранение токена и статусом CONFLICT
     */
    @ExceptionHandler(TokenNotSaveException.class)
    public ResponseEntity<String> handlerTokenNotSaveException(TokenNotSaveException ex)
    {
        logger.info("Ошибка: невозможно сохранить токен! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, невозможно сохранить токен
        return ResponseEntity.status(HttpStatus.CONFLICT).body("невозможно сохранение токена: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение TokenNotGenerateException.
     *
     * @param ex исключение, которое содержит информацию об ошибке
     * @return ResponseEntity с сообщением о том, что невозможна генерация токена и статусом CONFLICT
     */
    @ExceptionHandler(TokenNotGenerateException.class)
    public ResponseEntity<String> handlerTokenNotGenerateException(TokenNotGenerateException ex)
    {
        logger.info("Ошибка: невозможно сгенерировать токен! "); // Логирует сообщение об ошибке

        // Возвращает статус CONFLICT с сообщением о том, невозможно сгенерировать токен
        return ResponseEntity.status(HttpStatus.CONFLICT).body("невозможно сгенерировать токена: " + ex.getMessage());
    }
    //endRegion
}
