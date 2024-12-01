package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * Класс CustomUserDetailService:
 *
 * Реализует бизнес-логику для работы с пользовательскими данными, необходимыми для аутентификации и авторизации в приложении.
 * Отвечает за загрузку пользователя по email и преобразование данных в объект {@link org.springframework.security.core.userdetails.UserDetails}.
 *
 * Ключевые функции:
 * - Интеграция с базой данных через {@link UserRepository}.
 * - Реализация интерфейса {@link org.springframework.security.core.userdetails.UserDetailsService}.
 * - Загрузка и преобразование пользовательских данных.
 *
 * Зависимости:
 * - {@link UserRepository}: для доступа к данным пользователей в базе.
 * - {@link User}: объект, представляющий сущность пользователя приложения.
 *
 * Методы:
 * - {@link #loadUserByUsername(String)}: Загружает пользователя по email и возвращает объект {@link org.springframework.security.core.userdetails.UserDetails}.
 *
 * Исключения:
 * - {@link UsernameNotFoundException}: выбрасывается, если пользователь с указанным email не найден в базе данных.
 *
 * Аннотации:
 * - @Service: указывает, что класс является сервисным компонентом в Spring.
 */
@Service
public class CustomUserDetailService implements UserDetailsService
{
    //region Fields
    private final UserRepository userRepository; // Репозиторий для работы с пользователями в базе данных
    private User user; // Объект класса User
    //endRegion

    //region Constructors
    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param userRepository Репозиторий для получения пользователей из базы данных.
     * @param user Объект класса User
     */
    public CustomUserDetailService(UserRepository userRepository, User user)
    {
        this.userRepository = userRepository;
        this.user = user;
    }
    //endRegion

    //region Methods
    /**
     * Метод для загрузки пользователя по имени (в данном случае email).
     *
     * @param username Имя пользователя (email).
     * @return UserDetails с данными пользователя.
     * @throws UsernameNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByEmail(username); // Ищем пользователя по email
        if (user == null) // если пользователь равен null, то:
        {
            new UsernameNotFoundException("Пользователь с email " + username + " не найден!"); // В случае не нахождения пользователя обрабатываем исключение
        }
        // Преобразуем сущность пользователя в UserDetails с помощью пакета org.springframework.security.core.userdetails, который является частью библиотеки Spring Security.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // email как имя пользователя
                user.getPassword(), // пароль
                Collections.singletonList(user.getUserRole().getAuthority()) // роли пользователя
        );
    }
    //endRegion
}

