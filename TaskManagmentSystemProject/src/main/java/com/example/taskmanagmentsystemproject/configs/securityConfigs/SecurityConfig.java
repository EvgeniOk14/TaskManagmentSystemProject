package com.example.taskmanagmentsystemproject.configs.securityConfigs;

import com.example.taskmanagmentsystemproject.security.JwtAuthenticationEntryPoint;
import com.example.taskmanagmentsystemproject.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Класс SecurityConfig:
 *
 * Конфигурационный класс для настройки безопасности приложения.
 * Реализует настройку фильтров безопасности, аутентификации и авторизации,
 * а также обеспечивает интеграцию с JWT для управления сессиями и проверкой прав доступа.
 *
 * Основные функции:
 * - Настройка HTTP безопасности, включая доступ к различным URL на основе ролей и аутентификации.
 * - Интеграция фильтра JWT для проверки токенов на уровне запросов.
 * - Предоставление бинов для PasswordEncoder и AuthenticationManager.
 *
 * Зависимости:
 * - UserDetailsService: сервис для получения данных пользователя.
 * - JwtAuthenticationEntryPoint: обработчик ошибок аутентификации.
 * - JwtTokenFilter: фильтр для обработки JWT токенов.
 *
 * Аннотации:
 * - @Configuration: указывает, что класс содержит конфигурацию Spring.
 * - @Bean: создает и регистрирует бины в контексте Spring.
 */
@Configuration // Указывает, что класс является конфигурационным
public class SecurityConfig
{
    //region Fields
    private final UserDetailsService userDetailsService; // Сервис для получения данных пользователя
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Точка входа для обработки ошибок аутентификации
    private final JwtTokenFilter jwtTokenFilter; // Фильтр для обработки JWT токенов
    //endRegion

    //region Constructor
    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param userDetailsService          Сервис для получения данных пользователя.
     * @param jwtAuthenticationEntryPoint Точка входа для обработки ошибок аутентификации.
     * @param jwtTokenFilter              Фильтр для обработки JWT токенов.
     */
    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtTokenFilter jwtTokenFilter)
    {
        this.userDetailsService = userDetailsService; // Инициализация userDetailsService
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint; // Инициализация jwtAuthenticationEntryPoint
        this.jwtTokenFilter = jwtTokenFilter; // Инициализация jwtTokenFilter
    }
    //endRegion

    /**
     * Конфигурация фильтров безопасности и авторизации.
     * Настраивает правила доступа к URL, добавляет JWT фильтр и обрабатывает исключения.
     *
     * @param http HttpSecurity для настройки фильтров.
     * @return SecurityFilterChain конфигурация безопасности.
     * @throws Exception если возникает ошибка настройки безопасности.
     */
    @Bean // Указывает, что метод создает бин SecurityFilterChain
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Отключение защиты CSRF, так как используется JWT
                .authorizeHttpRequests(auth -> auth // Настройка правил авторизации
                        // Общедоступные эндпоинты
                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll() // Доступ к Swagger
                        .requestMatchers("/api/users/createUser").permitAll() // Доступ к созданию пользователей
                        .requestMatchers("/authz/checkAccess").permitAll() // Доступ к проверке прав доступа
                        .requestMatchers("/auth/**").permitAll() // Доступ к аутентификации

                        // Эндпоинты, доступные только администраторам
                        .requestMatchers("/admin/**").hasRole("ADMIN_ROLE") // Административные операции
                        .requestMatchers("/api/tasks/create").hasRole("ADMIN_ROLE") // Создание задач
                        .requestMatchers("/api/tasks/update/{id}").hasRole("ADMIN_ROLE") // Обновление задач
                        .requestMatchers("/api/tasks/delete/{id}").hasRole("ADMIN_ROLE") // Удаление задач
                        .requestMatchers("/api/tasks/{executorId}/{taskId}/setExecutorToTask").hasRole("ADMIN_ROLE") // Назначение исполнителя
                        .requestMatchers("/api/tasks/{authorId}/{taskId}/setAuthorToTask").hasRole("ADMIN_ROLE") // Назначение автора
                        .requestMatchers("/api/tasks/{id}/setStatus").hasRole("ADMIN_ROLE") // Установка статуса
                        .requestMatchers("/api/tasks/{id}/setPriority").hasRole("ADMIN_ROLE") // Установка приоритета

                        // Эндпоинты, доступные пользователям с ограничениями
                        .requestMatchers("/api/tasks/my-tasks").hasAnyRole("USER_ROLE", "ADMIN_ROLE") // Получение своих задач
                        .requestMatchers("/api/tasks/my-tasks/author-and-executor").hasAnyRole("USER_ROLE", "ADMIN_ROLE") // Задачи, где пользователь - автор и исполнитель
                        .requestMatchers("/api/comments/{taskId}/createComment").hasAnyRole("USER_ROLE", "ADMIN_ROLE") // Создание комментариев
                        .requestMatchers("/api/comments/{taskId}/getAllComments").hasAnyRole("USER_ROLE", "ADMIN_ROLE") // Просмотр комментариев
                        .requestMatchers("/api/comments/{commentId}/updateCommentById").hasAnyRole("USER_ROLE", "ADMIN_ROLE") // Редактирование комментариев
                        .requestMatchers("/api/comments/{commentId}/delete").hasAnyRole("USER_ROLE", "ADMIN_ROLE") // Удаление комментариев
                        .requestMatchers("/api/comments/{commentId}/getComment").hasAnyRole("USER_ROLE", "ADMIN_ROLE") // Просмотр конкретного комментария

                        // Эндпоинты, доступные всем аутентифицированным пользователям
                        .requestMatchers("/api/tasks/getTask/{id}").authenticated() // Просмотр задачи
                        .requestMatchers("/api/tasks/pagination").authenticated() // Список задач с пагинацией

                        // Остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Обработка ошибок аутентификации
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // Добавление JWT фильтра перед стандартным фильтром

        return http.build(); // Создание и возврат конфигурации безопасности
    }


    /**
     * Бин для AuthenticationManager.
     * Настраивает менеджер аутентификации с использованием UserDetailsService и PasswordEncoder.
     *
     * @param http HttpSecurity для извлечения AuthenticationManagerBuilder.
     * @return AuthenticationManager конфигурация для аутентификации.
     * @throws Exception если возникает ошибка при настройке.
     */
    @Bean // Указывает, что метод создает бин AuthenticationManager
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class); // Получение объекта для настройки аутентификации

        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // Настройка сервиса для загрузки данных пользователя
                .passwordEncoder(passwordEncoder()); // Настройка PasswordEncoder для хеширования паролей

        return authenticationManagerBuilder.build(); // Создание и возврат AuthenticationManager
    }

    /**
     * Бин для PasswordEncoder.
     * Предоставляет BCryptPasswordEncoder для хеширования паролей.
     *
     * @return PasswordEncoder объект для работы с паролями.
     */
    @Bean // Указывает, что метод создает бин PasswordEncoder
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(); // Возвращает объект BCryptPasswordEncoder для хеширования паролей
    }
}


