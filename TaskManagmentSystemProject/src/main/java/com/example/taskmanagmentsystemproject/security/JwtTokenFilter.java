package com.example.taskmanagmentsystemproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Класс JwtTokenFilter:
 *
 * Фильтр для обработки JWT токенов. Проверяет наличие токена в запросе и, если он действителен, устанавливает аутентификацию пользователя.
 *
 * Основные функции:
 * - Извлекает JWT токен из заголовка запроса.
 * - Проверяет валидность токена и его срок действия.
 * - Устанавливает аутентификацию пользователя в контексте Spring Security.
 *
 * Аннотации:
 * - {@link Component}: Указывает, что этот класс является компонентом Spring и будет автоматически обнаружен.
 *
 * Используемые зависимости:
 * - {@link JwtTokenUtil}: Утилита для работы с JWT токенами.
 * - {@link UserDetailsService}: Сервис для загрузки информации о пользователе.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter
{
    //region Fields
    private JwtTokenUtil jwtTokenUtil;  // Утилита для работы с JWT токенами
    private UserDetailsService userDetailsService;  // Сервис для загрузки деталей пользователя по имени
    //endRegion

    //region Constructor
    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param jwtTokenUtil         Утилита для работы с JWT токенами.
     * @param userDetailsService    Сервис для загрузки данных пользователя.
     */
    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService)
    {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }
    //endRegion

    //region Methods
    /**
     * Метод фильтрации запросов doFilterInternal.
     * Выполняется для каждого HTTP-запроса.
     * Извлекает JWT токен из заголовка, проверяет его валидность и если токен действителен,
     * Устанавливает аутентификацию пользователя в контексте безопасности.
     *
     * @param request  HTTP запрос.
     * @param response HTTP ответ.
     * @param chain     Цепочка фильтров.
     * @throws ServletException если возникает ошибка сервлета.
     * @throws IOException если возникает ошибка ввода/вывода.
     *
     * - Заголовок Authorization определен в спецификации HTTP/1.1 (RFC 7235) и является общепринятым способом
     *   передачи учетных данных для аутентификации.
     *   пример стандартного заголовка : Authorization: "Bearer <JWT_токен>"
     *   Bearer: Это схема аутентификации, указывающая, что токен является Bearer токеном.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        final String header = request.getHeader("Authorization"); // Извлекаем токен из заголовка Authorization

        if (header == null || !header.startsWith("Bearer ")) // Если токен отсутствует или не начинается с "Bearer", то: пропускаем запрос
        {
            chain.doFilter(request, response); // пропускаем запрос (передаем управление следующему фильтру в цепочке)
            return;  // выход
        }
        final String token = header.substring(7); // Извлекаем сам токен (т.е. начиная с седьмого символа, пропускаем "Bearer ")

        try
        {
            if (jwtTokenUtil.isTokenExpired(token)) // Проверяем, не истек ли токен
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Срок токена истёк. Пожалуйста пройдите аутентификацию снова!."); // Если токен истек, отправляем ошибку и сообщаем, что нужно пройти повторную аутентификацию
                return; //выход
            }

            String username = jwtTokenUtil.extractUsername(token); // Извлекаем имя пользователя из токена

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) // Проверяем, что имя пользователя не пустое и что в контексте безопасности нет аутентификации
            {
                // Загружаем детали пользователя по его имени, из базы данных
                // (описанного в application.yml) с помощью метода loadUserByUsername интерфейса userDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.isTokenValid(token, userDetails)) // Проверяем валидность токена для пользователя, если токен валиден, то:
                {
                    // Создаем объект аутентификации:
                    // создается объект UsernamePasswordAuthenticationToken, который представляет аутентификацию пользователя
                    //  Этот объект содержит:
                    // userDetails — детали пользователя, загруженные ранее.
                    // null — здесь обычно указывается пароль, но он не нужен после успешной аутентификации через токен.
                    // userDetails.getAuthorities() — роли и привилегии пользователя.
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Устанавливаем дополнительные детали аутентификации,
                    // например, IP-адрес, с которого был отправлен запрос.
                    // Это может быть полезно для аудита или дополнительной безопасности.
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Устанавливаем аутентификацию в контекст безопасности
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (Exception e) // обрабатываем исключение в случае ошибки при обработке токена
        {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Некорректный токен."); // В случае ошибки при обработке токена, отправляем ошибку
            return; // выход
        }
        chain.doFilter(request, response); // Передаем управление следующему фильтру в цепочке
    }
    //endRegion
}


