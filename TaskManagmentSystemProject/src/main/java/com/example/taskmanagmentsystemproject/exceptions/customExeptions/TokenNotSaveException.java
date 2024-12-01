package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - TokenNotSaveException
 * Исключение, возникающее при невозможности по каким-либо причинам сохранение токена
 */
public class TokenNotSaveException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения TokenNotSaveException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public TokenNotSaveException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
