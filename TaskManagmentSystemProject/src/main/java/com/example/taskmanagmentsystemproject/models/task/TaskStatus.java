package com.example.taskmanagmentsystemproject.models.task;

/**
 * Класс TaskStatus - представляет статус задачи в системе управления задачами.
 *
 * Описание класса TaskStatus:
 * Перечисление используется для определения текущего состояния задачи.
 * Доступные уровни:
 *   PENDING - задача находится в ожидании выполнения
 *   IN_PROGRESS - задача в процессе выполнения
 *   COMPLETED - задача завершена
 *
 * Статус используется для контроля выполнения задач и отображения их состояния в системе.
 */
public enum TaskStatus
{
    PENDING,
    IN_PROGRESS,
    COMPLETED
}
