package com.example.taskmanagmentsystemproject.repository;

import com.example.taskmanagmentsystemproject.models.token.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс JwtTokenRepository:
 *
 * Отвечает за доступ к данным JWT-токенов в базе данных MongoDB. Расширяет интерфейс MongoRepository, предоставляя стандартные методы CRUD.
 * Включает дополнительные методы для поиска и удаления токенов на основе их срока действия и привязки к пользователю.
 *
 * Основные функции:
 * - Использует аннотацию @Repository через наследование от Spring Data MongoRepository.
 * - Позволяет выполнять операции с JWT-токенами, такие как поиск по userId и удаление по сроку действия.
 *
 * Методы:
 * - {@link #findByUserId(Integer)}: Ищет JWT-токен по идентификатору пользователя.
 * - {@link #deleteByExpirationDateBefore(Date)}: Удаляет токены, срок действия которых истёк до указанной даты.
 * - {@link #findByExpirationDateBefore(Date)}: Находит токены, срок действия которых истёк до указанной даты.
 */
public interface JwtTokenRepository extends MongoRepository<JwtToken, String>
{
    //region Methods
    /**
     * Метод findByUserId:
     * Ищет JWT-токен по идентификатору пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Optional, содержащий JWT-токен, если он найден.
     */
    Optional<JwtToken> findByUserId(Integer userId);

    /**
     * Метод findByExpirationDateBefore:
     * Находит токены, срок действия которых истёк до указанной даты.
     *
     * @param date Дата, до которой токены считаются просроченными.
     * @return Список токенов, срок действия которых истёк.
     */
    List<JwtToken> findByExpirationDateBefore(Date date);

    /**
     * Метод deleteByExpirationDateBefore:
     * Удаляет токены, срок действия которых истёк до указанной даты.
     *
     * @param date Дата, до которой токены считаются просроченными.
     */
    void deleteByExpirationDateBefore(Date date);
    //endRegion
}
