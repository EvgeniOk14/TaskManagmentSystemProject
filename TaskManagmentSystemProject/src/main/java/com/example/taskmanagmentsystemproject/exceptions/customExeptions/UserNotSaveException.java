package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Исключение UserNotSaveException, возникающее при попытке сохранения пользователя в базу данных
 */
public class UserNotSaveException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения UserNotSaveException.
     *
     * @param message сообщение, которое будет передано в качестве причины исключения
     */
    public UserNotSaveException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}


