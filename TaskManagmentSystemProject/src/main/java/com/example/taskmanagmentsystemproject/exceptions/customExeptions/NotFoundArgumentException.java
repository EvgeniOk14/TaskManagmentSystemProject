package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - NotFoundArgumentException
 * Исключение, возникающее при не нахождении какого либо аргумента  у какого-либо объекта
 */
public class NotFoundArgumentException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения NotFoundArgumentException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public NotFoundArgumentException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
