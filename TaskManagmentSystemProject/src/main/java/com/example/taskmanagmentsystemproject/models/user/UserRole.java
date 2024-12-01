package com.example.taskmanagmentsystemproject.models.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Перечисление, представляющее роли пользователя в системе.
 *
 * Это перечисление определяет роли пользователей, которые могут быть присвоены пользователям
 * в системе безопасности. Каждая роль может быть преобразована в объект типа {@link GrantedAuthority},
 * который используется в Spring Security для контроля доступа.
 */
public enum UserRole
{
    /**
     * Роль администратора.
     * Пользователь с этой ролью имеет привилегии администратора в системе.
     */
    ADMIN_ROLE,

    /**
     * Роль обычного пользователя.
     * Пользователь с этой ролью имеет стандартные привилегии в системе.
     */
    USER_ROLE;


    /**
     * Метод для получения роли как {@link GrantedAuthority}.
     *
     * Этот метод преобразует роль в объект типа {@link GrantedAuthority}, который используется в Spring Security.
     * Роль преобразуется в строку с префиксом "ROLE_" (например, "ROLE_ADMIN_ROLE").
     * Это необходимо для совместимости с Spring Security, который ожидает такой формат для ролей.
     *
     * @return {@link GrantedAuthority}, представляющий роль в системе.
     */
    public GrantedAuthority getAuthority()
    {
        // Возвращаем роль с префиксом "ROLE_" (например, "ROLE_ADMIN_ROLE" или "ROLE_USER_ROLE")
        return new SimpleGrantedAuthority("ROLE_" + this.name()); // Преобразуем в "ROLE_ADMIN_ROLE"
    }
}
