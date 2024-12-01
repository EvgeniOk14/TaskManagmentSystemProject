package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - PasswordEmptyException
 * Исключение, возникающее при попытке валидации пароля
 */
public class PasswordEmptyException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения PasswordEmptyException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public PasswordEmptyException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
