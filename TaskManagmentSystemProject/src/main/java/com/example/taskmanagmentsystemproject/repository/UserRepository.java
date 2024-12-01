package com.example.taskmanagmentsystemproject.repository;

import com.example.taskmanagmentsystemproject.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс UserRepository:
 *
 * Отвечает за доступ к данным пользователей в базе данных. Расширяет интерфейс JpaRepository, предоставляя стандартные методы CRUD.
 * Включает дополнительные методы для поиска пользователей по email и идентификатору.
 *
 * Основные функции:
 * - Использует аннотацию @Repository для интеграции с Spring.
 * - Предоставляет возможность поиска пользователей по их уникальным атрибутам.
 *
 * Методы:
 * - {@link #findByEmail(String)}: Ищет пользователя по его email.
 * - {@link #findUserById(Integer)}: Ищет пользователя по его идентификатору.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    //region Methods
    /**
     * Метод findByEmail:
     * Ищет пользователя по его email.
     *
     * @param email Email пользователя.
     * @return объект User, представляющий найденного пользователя.
     * @exception IllegalArgumentException выбрасывается, если передан некорректный email.
     */
    User findByEmail(String email);

    /**
     * Метод findUserById:
     * Ищет пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return объект User, представляющий найденного пользователя.
     * @exception IllegalArgumentException выбрасывается, если передан некорректный ID.
     */
    User findUserById(Integer id);
    //endRegion
}
