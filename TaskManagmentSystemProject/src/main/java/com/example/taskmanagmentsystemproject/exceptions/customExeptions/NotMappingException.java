package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - NotMappingException
 * Исключение, возникающее при проблемах маппинга одной сущности в другую
 */
public class NotMappingException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения NotMappingException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public NotMappingException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
