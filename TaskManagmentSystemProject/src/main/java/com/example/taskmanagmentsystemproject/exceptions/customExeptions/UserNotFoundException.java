package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Исключение UserNotFoundException, возникающее при попытке сохранения пользователя в базу данных
 */
public class UserNotFoundException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения UserNotFoundException.
     *
     * @param message сообщение, которое будет передано в качестве причины исключения
     */
    public UserNotFoundException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException

    }
    //endRegion
}



