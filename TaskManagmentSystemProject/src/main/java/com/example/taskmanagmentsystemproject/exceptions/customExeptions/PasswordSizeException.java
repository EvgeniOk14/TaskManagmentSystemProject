package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - PasswordSizeException
 * Исключение, возникающее при попытке валидации длины пароля (не менее 5 символов)
 */
public class PasswordSizeException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения PasswordSizeException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public PasswordSizeException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
