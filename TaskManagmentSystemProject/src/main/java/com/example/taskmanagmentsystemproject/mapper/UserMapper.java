package com.example.taskmanagmentsystemproject.mapper;

import com.example.taskmanagmentsystemproject.dto.UserDTO;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.serviceDTO.EntityDTOMapper;
import org.mapstruct.Mapper;

/**
 * Интерфейс UserMapper:
 *
 * Используется для преобразования между сущностью {@link User} и объектом передачи данных (DTO) {@link UserDTO}.
 * Этот интерфейс основан на библиотеке MapStruct, которая автоматически генерирует реализацию методов преобразования,
 * предоставляемых интерфейсом {@link EntityDTOMapper}.
 *
 * Основная цель:
 * Упрощение преобразований между сущностью и DTO, избавление от ручного написания кода маппинга,
 * а также обеспечение стандартизации и читаемости кода.
 *
 * Аннотации:
 * - {@code @Mapper}: Указывает, что интерфейс является MapStruct-маппером. Автоматически генерирует реализацию
 *   в процессе компиляции.
 * - {@code @Override}: Переопределяет методы базового интерфейса {@link EntityDTOMapper}.
 *
 * Особенности:
 * - Полная интеграция со Spring через атрибут {@code componentModel = "spring"}.
 * - Гибкость и удобство работы с сущностями и DTO благодаря универсальным методам.
 * - Исключение {@link IllegalArgumentException} выбрасывается в случае, если входной параметр равен {@code null}.
 */
@Mapper(componentModel = "spring") // Аннотация MapStruct для генерации реализации и интеграции со Spring
public interface UserMapper extends EntityDTOMapper<User, UserDTO>
{
    /**
     * Метод toDTO:
     * Преобразует сущность User в объект DTO UserDTO.
     * Реализация метода будет автоматически сгенерирована MapStruct.
     * @param entity объект сущности User, который нужно преобразовать. // Сущность для преобразования.
     * @return объект DTO UserDTO, соответствующий переданной сущности. // Результат преобразования.
     * @throws IllegalArgumentException если переданный объект сущности равен null. // Исключение при null.
     */
    @Override
    UserDTO toDTO(User entity); // Метод для преобразования User -> UserDTO.

    /**
     * Преобразует объект DTO {@link UserDTO} в сущность {@link User}.
     *
     * Реализация метода будет автоматически сгенерирована MapStruct.
     *
     * @param dto объект {@link UserDTO}, который нужно преобразовать. // DTO для преобразования.
     * @return объект сущности {@link User}, соответствующий переданному DTO. // Результат преобразования.
     * @throws IllegalArgumentException если переданный объект DTO равен {@code null}. // Исключение при null.
     */
    @Override
    User toEntity(UserDTO dto); // Метод для преобразования UserDTO -> User.
}
