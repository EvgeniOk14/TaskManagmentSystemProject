package com.example.taskmanagmentsystemproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс LoginRequestDTO:
 *
 * Представляет объект передачи данных (DTO) для запроса на вход пользователя.
 * Используется для передачи информации, необходимой для аутентификации пользователя.
 *
 * Наследование:
 * - Наследуется от {@link BaseUserDTO}, что обеспечивает доступ к общим полям, таким как email.
 *
 * Основные атрибуты:
 * - Электронная почта пользователя (email).
 * - Пароль пользователя (password).
 *
 * Аннотации:
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 *
 * Поля:
 * - {@link #email}: адрес электронной почты пользователя, обязательное поле с проверкой формата.
 * - {@link #password}: пароль пользователя, обязательное поле.
 *
 * Валидация:
 * - Поле `email` не может быть пустым и должно быть в правильном формате, проверяется через аннотации {@link NotEmpty} и {@link Email}.
 * - Поле `password` также не может быть пустым, проверяется через аннотацию {@link NotEmpty}.
 */
@Data // библиотека Lombok: добавляет стандартные методы (геттеры, сеттеры и т.д.)
@NoArgsConstructor // Конструктор без аргументов
@AllArgsConstructor // Конструктор со всеми аргументами
public class LoginRequestDTO extends BaseUserDTO
{
    //region Fields
    /**
     * Электронная почта пользователя.
     * Не может быть пустым и должна соответствовать правильному формату.
     */
    @NotEmpty(message = "Email не может быть пустым!")
    @Email(message = "Не верный формат почты!")
    private String email;

    /**
     * Пароль пользователя.
     * Обязательное поле, не может быть пустым.
     */
    @NotEmpty(message = "Пароль не может быть пустым!")
    private String password;
    //endRegion
}
