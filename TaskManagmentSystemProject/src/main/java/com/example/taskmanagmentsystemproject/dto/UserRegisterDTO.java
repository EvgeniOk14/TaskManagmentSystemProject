package com.example.taskmanagmentsystemproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс UserRegisterDTO:
 *
 * Представляет объект передачи данных (DTO) для регистрации пользователя.
 * Содержит информацию, необходимую для регистрации нового пользователя в системе.
 * Включает в себя поля для электронной почты, пароля и роли пользователя.
 *
 * Основные атрибуты:
 * - Электронная почта пользователя (email), унаследованная от {@link BaseUserDTO}.
 * - Пароль пользователя (password).
 * - Роль пользователя (userRole).
 *
 * Аннотации:
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 *
 * Валидация:
 * - {@link #password}: не может быть пустым и должен содержать минимум 5 символов.
 * - {@link #userRole}: не может быть пустым.
 *
 * Поля:
 * -  email: адрес электронной почты пользователя (унаследовано от {@link BaseUserDTO}).
 * - {@link #password}: пароль пользователя для регистрации.
 * - {@link #userRole}: роль пользователя в системе (например, ADMIN_ROLE или USER_ROLE).
 */
@Data // библиотека Lombok: добавляет стандартные методы (геттеры, сеттеры и т.д.)
@AllArgsConstructor // Конструктор со всеми аргументами
@NoArgsConstructor // Конструктор без аргументов
public class UserRegisterDTO extends BaseUserDTO
{
    //region Fields
    /**
     * Пароль пользователя.
     * Не может быть пустым и должен содержать минимум 5 символов.
     */
    @NotEmpty(message = "Пароль не может быть пустым! ")
    @Size(min = 5, message = "Пароль должен содержать минимум 5 символов!" )
    private String password; // Пароль пользователя для регистрации.

    /**
     * Роль пользователя в системе.
     * Например, ADMIN_ROLE для администратора и USER_ROLE для обычного пользователя.
     */
    @NotEmpty(message = "Роль не может быть пустой! ")
    private String userRole; // Роль пользователя в системе (например, ADMIN_ROLE, USER_ROLE).
    //endRegion
}

