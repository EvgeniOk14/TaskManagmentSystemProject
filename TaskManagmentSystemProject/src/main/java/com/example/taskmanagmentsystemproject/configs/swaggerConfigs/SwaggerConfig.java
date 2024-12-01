package com.example.taskmanagmentsystemproject.configs.swaggerConfigs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Класс SwaggerConfig:
 *
 * Конфигурационный класс для настройки документации API с использованием Swagger и OpenAPI.
 * Этот класс конфигурирует и настраивает отображение информации о Task Management API,
 * а также добавляет поддержку аутентификации с помощью JWT Bearer токенов.
 *
 * Основные функции:
 * - Настройка информации о API, включая название, версию и описание.
 * - Добавление схемы безопасности для использования JWT токенов в API.
 *
 * Зависимости:
 * - OpenAPI: для конфигурации Swagger/OpenAPI документации.
 * - SecurityScheme: для определения схемы безопасности с использованием JWT.
 *
 * Аннотации:
 * - @Configuration: указывает, что класс является конфигурационным классом Spring, который содержит бины.
 * - @Bean: используется для определения бина, который будет доступен в контексте Spring.
 *
 * Архитектурные особенности:
 * - Использует OpenAPI для конфигурации документации Swagger.
 * - Обеспечивает поддержку аутентификации с помощью JWT токенов.
 *
 * Аннотации:
 * - @Configuration: указывает, что класс является конфигурационным классом Spring.
 */
@Configuration // Указывает, что это конфигурационный класс Spring
public class SwaggerConfig
{
    /**
     * Метод customOpenAPI:
     * Конфигурирует и возвращает объект OpenAPI для настройки документации Swagger.
     * Настроена информация о API, включая название, описание и версию.
     * Также добавляется схема безопасности для использования JWT токенов.
     *
     * @return Объект OpenAPI, настроенный с необходимыми параметрами.
     */
    @Bean // Указывает, что метод создает и возвращает бин, который будет доступен в контексте Spring.
    public OpenAPI customOpenAPI()
    {
        final String securitySchemeName = "bearerAuth"; // Имя схемы безопасности

        return new OpenAPI() // Создание нового объекта OpenAPI для настройки Swagger
                .info(new Info() // Добавление информации о API
                        .title("Task Management API") // Название API
                        .version("1.0") // Версия API
                        .description("API documentation for Task Management System")) // Описание API
                // Добавляем глобальную схему безопасности
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // Добавление схемы безопасности на уровне всей документации
                .components(new io.swagger.v3.oas.models.Components() // Добавление компонентов, включая схемы безопасности
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme() // Добавление схемы безопасности с именем "bearerAuth"
                                .name(securitySchemeName) // Название схемы
                                .type(SecurityScheme.Type.HTTP) // Тип схемы безопасности — HTTP
                                .scheme("bearer") // Тип аутентификации — Bearer
                                .bearerFormat("JWT"))); // Формат токена — JWT
    }
}


