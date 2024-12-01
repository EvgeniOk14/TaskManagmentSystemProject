package com.example.taskmanagmentsystemproject.mapper;

import com.example.taskmanagmentsystemproject.dto.UserRegisterDTO;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.serviceDTO.EntityDTOMapper;
import org.mapstruct.Mapper;

/**
 * Интерфейс UserRegisterMapper:
 *
 * Используется для преобразования между сущностью {@link User} и объектом передачи данных (DTO) {@link UserRegisterDTO}.
 * Этот интерфейс реализует функциональность маппинга с использованием библиотеки MapStruct, которая автоматически
 * генерирует реализацию методов преобразования в процессе компиляции.
 *
 * Основное предназначение:
 * Предоставить средства для преобразования данных при регистрации пользователей, чтобы разделить модель сущности
 * и данные, необходимые для передачи или получения от клиента.
 *
 * Аннотации:
 * - {@code @Mapper}: Указывает, что интерфейс является MapStruct-маппером. Реализация генерируется автоматически.
 * - {@code componentModel = "spring"}: Интегрирует генерируемый маппер с Spring, чтобы его можно было инжектировать
 *   через контекст.
 *
 * Особенности:
 * - Маппинг между сущностью {@link User} и DTO {@link UserRegisterDTO}.
 * - Генерация реализации методов преобразования, избавляя разработчиков от ручного написания кода маппинга.
 * - Простота и гибкость использования в любых слоях приложения.
 */
@Mapper(componentModel = "spring")
public interface UserRegisterMapper extends EntityDTOMapper<User, UserRegisterDTO>
{
    // Автоматическое преобразование User <-> UserRegisterDTO
    // MapStruct сгенерирует методы:
    // - toDTO(User entity) -> UserRegisterDTO
    // - toEntity(UserRegisterDTO dto) -> User

    /**
     * Преобразует объект сущности {@link User} в объект DTO {@link UserRegisterDTO}.
     *
     * Этот метод предназначен для подготовки данных сущности пользователя к передаче клиенту
     * или для использования в процессе регистрации.
     * Реализация метода будет автоматически сгенерирована MapStruct.
     *
     * @param entity объект сущности {@link User}, который нужно преобразовать. // Входная сущность.
     * @return объект DTO {@link UserRegisterDTO}, соответствующий переданной сущности. // Результат преобразования.
     * @throws IllegalArgumentException если переданный объект сущности равен {@code null}. // Исключение при null.
     */
    //@Override
    //UserRegisterDTO toDTO(User entity); // Метод для преобразования User -> UserRegisterDTO.

    /**
     * Преобразует объект DTO {@link UserRegisterDTO} в объект сущности {@link User}.
     *
     * Этот метод используется для преобразования данных, полученных от клиента, в формат сущности,
     * пригодной для сохранения в базе данных или обработки в бизнес-логике.
     * Реализация метода будет автоматически сгенерирована MapStruct.
     *
     * @param dto объект {@link UserRegisterDTO}, который нужно преобразовать. // Входной DTO.
     * @return объект сущности {@link User}, соответствующий переданному DTO. // Результат преобразования.
     * @throws IllegalArgumentException если переданный объект DTO равен {@code null}. // Исключение при null.
     */
    //@Override
    //User toEntity(UserRegisterDTO dto); // Метод для преобразования UserRegisterDTO -> User.
}

