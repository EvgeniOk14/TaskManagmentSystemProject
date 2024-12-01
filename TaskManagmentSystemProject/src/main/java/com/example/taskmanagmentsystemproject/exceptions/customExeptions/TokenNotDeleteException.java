package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - TokenNotDeleteException
 * Исключение, возникающее при невозможности по каким-либо причинам удаление токена
 */
public class TokenNotDeleteException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения TokenNotDeleteException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public TokenNotDeleteException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
