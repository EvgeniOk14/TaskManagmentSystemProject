package com.example.taskmanagmentsystemproject.models.tokenCleanupScheduler;

import com.example.taskmanagmentsystemproject.services.JwtTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Класс TokenCleanupScheduler:
 *
 * Компонент для автоматической очистки устаревших JWT токенов в системе.
 * Отвечает за периодический запуск задачи удаления истекших токенов из хранилища.
 *
 * Ключевые функции:
 * - Запуск задачи очистки с заданным интервалом времени.
 * - Использование службы JwtTokenService для выполнения операций очистки.
 *
 * Зависимости:
 * - JwtTokenService: сервис для работы с JWT токенами.
 *
 * Методы:
 * - {@link #cleanupExpiredTokens()}: Периодически удаляет устаревшие токены из хранилища.
 *
 * Аннотации:
 * - @Component: указывает, что класс является компонентом Spring.
 * - @Scheduled: задает расписание для выполнения метода очистки.
 */
@Component
public class TokenCleanupScheduler
{
    //region Fields
    private JwtTokenService jwtTokenService; // Сервис для работы с JWT токенами
    //endRegion

    //region Constructor
    /**
     * Конструктор TokenCleanupScheduler:
     * Инициализирует класс с указанной зависимостью JwtTokenService.
     *
     * @param jwtTokenService объект сервиса для управления JWT токенами.
     */
    public TokenCleanupScheduler(JwtTokenService jwtTokenService)
    {
        this.jwtTokenService = jwtTokenService; // Инициализируем зависимость jwtTokenService.
    }
    //endRegion

    //region Methods
    /**
     * Метод cleanupExpiredTokens:
     * Периодически удаляет устаревшие токены из хранилища.
     * Запускается каждые 10 минут благодаря аннотации @Scheduled.
     *
     * @exception  IllegalStateException если удаление токенов невозможно из-за внутренней ошибки сервиса.
     */
    @Scheduled(fixedRate = 600000) // Метод будет запускаться каждые 10 минут.
    public void cleanupExpiredTokens()
    {
        jwtTokenService.deleteExpiredTokens(); // Вызываем метод сервиса для удаления устаревших токенов.
    }
    //endRegion
}

