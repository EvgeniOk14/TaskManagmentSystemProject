package com.example.taskmanagmentsystemproject.models.task;

/**
 * Класс TaskPriority - представляет приоритет задачи в системе управления задачами.
 *
 * Описание класса TaskPriority:
 * Перечисление используется для задания уровня важности задачи.
 * Доступные уровни:
 *   HIGH - высокий приоритет
 *   MEDIUM - средний приоритет
 *   LOW - низкий приоритет
 *
 * Приоритет используется для сортировки или фильтрации задач по важности.
 */
public enum TaskPriority
{
    HIGH,
    MEDIUM,
    LOW
}
