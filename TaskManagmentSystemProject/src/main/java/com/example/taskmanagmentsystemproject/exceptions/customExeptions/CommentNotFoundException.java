package com.example.taskmanagmentsystemproject.exceptions.customExeptions;


/**
 * Метод - CommentNotFoundException
 * Исключение, возникающее при не нахождении комментария по заданному id в базе данных
 */
public class CommentNotFoundException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения CommentNotFoundException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public CommentNotFoundException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
