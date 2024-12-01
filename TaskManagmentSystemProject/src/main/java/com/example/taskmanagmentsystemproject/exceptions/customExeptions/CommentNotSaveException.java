package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - CommentNotSaveException
 * Исключение, возникающее при попытке сохранения комментария в БАЗУ ДАННЫХ
 */
public class CommentNotSaveException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения CommentNotSaveException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public CommentNotSaveException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}

