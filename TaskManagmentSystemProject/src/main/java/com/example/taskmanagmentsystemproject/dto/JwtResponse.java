package com.example.taskmanagmentsystemproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Класс JwtResponse:
 *
 * Представляет объект передачи данных (DTO) для ответа сервера с JWT-токеном.
 * Используется для передачи токена аутентификации и дополнительной информации о пользователе клиенту.
 *
 * Основные атрибуты:
 * - JWT токен (token).
 * - Электронная почта пользователя (email) (необязательно).
 * - Роль пользователя (role) (необязательно).
 *
 * Аннотации:
 * - @Data: добавляет геттеры, сеттеры и другие методы через библиотеку Lombok.
 * - @AllArgsConstructor: создает конструктор со всеми аргументами.
 * - @NoArgsConstructor: создает конструктор без аргументов.
 *
 * Поля:
 * - {@link #token}: JWT токен, используемый для аутентификации и авторизации.
 * - {@link #email}: адрес электронной почты пользователя, связанного с токеном.
 * - {@link #role}: роль пользователя, связанная с текущим токеном.
 *
 * Особенности:
 * - Поле serialVersionUID используется для обеспечения совместимости при сериализации и десериализации объекта.
 *
 * Конструкторы:
 * - {@link #JwtResponse(String token)}: упрощенный конструктор для передачи только токена.
 */
@Data // библиотека Lombok: добавляет стандартные методы (геттеры, сеттеры и т.д.)
@AllArgsConstructor // Конструктор со всеми аргументами
@NoArgsConstructor  // Конструктор без аргументов
public class JwtResponse implements Serializable
{
    //region Fields
    private static final long serialVersionUID = 1L; // Уникальный идентификатор для сериализации. Гарантирует совместимость между различными версиями класса.
    private String token; // JWT токен. Используется для аутентификации пользователя.
    private String email; // Электронная почта пользователя. Необязательное поле, содержащее информацию о текущем пользователе.
    private String role; // Роль пользователя. Необязательное поле, определяющее права доступа пользователя.
    //endRegion

    //region Constructor
    /**
     * Конструктор для создания ответа, содержащего только JWT токен.
     *
     * @param token JWT токен.
     */
    public JwtResponse(String token)
    {
        this.token = token;
    }
    //endRegion
}
