package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - CommentNotDeleteException
 * Исключение, возникающее при невозможности по каким-либо причинам удаление комментария по заданному id из базы данных
 */
public class CommentNotDeleteException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения CommentNotDeleteException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public CommentNotDeleteException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}


