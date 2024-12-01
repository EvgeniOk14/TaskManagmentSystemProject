package com.example.taskmanagmentsystemproject.exceptions.customExeptions;


/**
 * Метод - TokenNotGenerateException
 * Исключение, возникающее при невозможности по каким-либо причинам сгенерировать токен
 */
public class TokenNotGenerateException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения TokenNotGenerateException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public TokenNotGenerateException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
