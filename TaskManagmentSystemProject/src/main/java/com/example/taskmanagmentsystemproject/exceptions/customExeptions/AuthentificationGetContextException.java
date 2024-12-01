package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - AuthentificationGatContextException
 * Исключение, возникающее при не нахождении данных при извлечении из контекста аутентификации
 */
public class AuthentificationGetContextException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения AuthentificationGatContextException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public AuthentificationGetContextException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
