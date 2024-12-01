package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - FailEncodePasswordException
 * Исключение, возникающее при попытке кодировки пароля
 */
public class FailEncodePasswordException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения FailEncodePasswordException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public FailEncodePasswordException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}

