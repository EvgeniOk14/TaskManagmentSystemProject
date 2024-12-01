package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - MethodArgumentNotValidException
 * Исключение, возникающее при неверного аргумента
 */
public class MethodArgumentNotValidException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения MethodArgumentNotValidException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public MethodArgumentNotValidException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
