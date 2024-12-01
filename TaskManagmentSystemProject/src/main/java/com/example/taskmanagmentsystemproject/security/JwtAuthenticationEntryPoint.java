package com.example.taskmanagmentsystemproject.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Класс JwtAuthenticationEntryPoint:
 *
 * Обработчик неавторизованных запросов. Используется в механизме безопасности Spring Security.
 * Срабатывает при попытке доступа к защищенному ресурсу без аутентификации.
 *
 * Основные функции:
 * - Отправляет клиенту HTTP-ответ с кодом 401 (Unauthorized).
 * - Позволяет информировать клиента о необходимости пройти аутентификацию.
 *
 *  Методы:
 *  * - {@link #commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}: Выполняет обработку неавторизованного запроса, Отправляет клиенту ответ с кодом 401 (Unauthorized)
 *
 * Аннотации:
 * - {@link Component}: Указывает, что этот класс является компонентом Spring и может быть использован как часть конфигурации безопасности.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    /**
     * Выполняет обработку неавторизованного запроса.
     * Отправляет клиенту ответ с кодом 401 (Unauthorized).
     *
     * @param request       HTTP-запрос
     * @param response      HTTP-ответ
     * @param authException Исключение, связанное с отсутствием аутентификации
     * @throws IOException Если возникает ошибка ввода/вывода
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException
    {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); // Устанавливаем код ошибки 401 (Unauthorized) в ответе
    }
}

