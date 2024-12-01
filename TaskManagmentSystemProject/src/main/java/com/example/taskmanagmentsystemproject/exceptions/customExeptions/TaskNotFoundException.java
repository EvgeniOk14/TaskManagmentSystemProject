package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - TaskNotFoundException
 * Исключение, возникающее при не нахождении задачи по заданному id в базе данных
 */
public class TaskNotFoundException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения TaskNotFoundException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public TaskNotFoundException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
