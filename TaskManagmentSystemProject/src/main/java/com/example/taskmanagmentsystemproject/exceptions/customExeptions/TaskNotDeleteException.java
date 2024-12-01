package com.example.taskmanagmentsystemproject.exceptions.customExeptions;

/**
 * Метод - TaskNotDeleteException
 * Исключение, возникающее при невозможности по каким-либо причинам удаление задачи по заданному id из базы данных
 */
public class TaskNotDeleteException extends RuntimeException
{
    //region Constructor
    /**
     * Конструктор для создания исключения TaskNotDeleteException.
     *
     * @param message сообщение, которое будет передано пользователю в качестве причины исключения
     */
    public TaskNotDeleteException(String message)
    {
        super(message); // Передаёт сообщение родительскому классу RuntimeException
    }
    //endRegion
}

