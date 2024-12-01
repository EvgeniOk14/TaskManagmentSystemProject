package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - TaskNotSaveExeption
 * Исключение, возникающее при попытке сохранения задачи в БАЗУ ДАННЫХ
 */
public class TaskNotSaveExсeption extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения TaskNotSaveExeption.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public TaskNotSaveExсeption(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}
