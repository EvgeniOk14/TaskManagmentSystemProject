package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Исключение, возникающее при попытке получения email у пользователя (email равен null, либо isEmpty)
 */
public class EmailIsNullExeption  extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения EmailIsNullExeption.
     *
     * @param message сообщение, которое будет передано в качестве причины исключения
     */
    public EmailIsNullExeption(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
