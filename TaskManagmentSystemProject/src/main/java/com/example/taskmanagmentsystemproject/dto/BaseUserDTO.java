package com.example.taskmanagmentsystemproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Абстрактный класс BaseUserDTO:
 *
 * Представляет базовый объект передачи данных (DTO) для пользователя.
 * Содержит общее поле, которое может быть использовано и унаследовано другими DTO-классами.
 *
 * Основные атрибуты:
 * - Электронная почта (email) для идентификации пользователя.
 *
 * Аннотации:
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 *
 * Поля:
 * - {@link #email}: адрес электронной почты пользователя.
 */
@Data // библиотека Lombok: добавляет стандартные методы (геттеры, сеттеры и т.д.)
@AllArgsConstructor // Конструктор со всеми аргументами
@NoArgsConstructor  // Конструктор без аргументов
public abstract class BaseUserDTO
{
    //region Fields
    /**
     * Электронная почта пользователя.
     * Используется для идентификации пользователя в системе.
     */
    private String email; // поле для хранения email
    //endRegion
}
