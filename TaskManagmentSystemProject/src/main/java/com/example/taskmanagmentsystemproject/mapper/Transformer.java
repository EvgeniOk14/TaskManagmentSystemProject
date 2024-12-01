package com.example.taskmanagmentsystemproject.mapper;

import com.example.taskmanagmentsystemproject.serviceDTO.EntityDTOMapper;

/**
 * Интерфейс Transformer:
 *
 * Обеспечивает универсальные методы для преобразования между сущностью (Entity) и объектом передачи данных (DTO).
 * Цель интерфейса — обеспечить гибкость и независимость от конкретных типов сущностей и DTO, используя дженерики.
 * Реализация интерфейса должна обеспечить функционал для преобразования как из сущностей в DTO, так и обратно.
 *
 * Методы интерфейса:
 * - transformFromEntityToDTO: Преобразует объект сущности (Entity) в объект передачи данных (DTO).
 * - transformFromDTOToEntity: Преобразует объект передачи данных (DTO) в объект сущности (Entity).
 *
 * Основная идея:
 * Интерфейс позволяет преобразовывать сущности в DTO и обратно, используя мапперы, реализующие интерфейс EntityDTOMapper,
 * что дает гибкость для работы с любыми типами сущностей и DTO.
 *
 * Пример использования:
 * - Использование этого интерфейса позволяет преобразовывать объекты различных типов (например, User, Task) в их соответствующие DTO
 * и обратно, используя универсальные методы и мапперы, что помогает избежать дублирования кода и улучшает читаемость.
 *
 * @param {E}: Тип сущности (например, User, Task), которая будет преобразована.
 * @param {D}: Тип DTO (например, UserDTO, TaskDTO), который будет создан.
 */
public interface Transformer
{
    /**
     * Метод transformFromEntityToDTO:
     * Преобразует объект сущности (Entity) в объект передачи данных (DTO).
     *
     * @param <E> тип сущности, которая будет преобразована (например, User, Task и т.д.).
     * @param <D> тип DTO, который будет возвращён (например, UserDTO, TaskDTO и т.д.).
     * @param entity объект сущности, который нужно преобразовать в DTO. Не может быть null.
     * @param mapper объект маппера, реализующий интерфейс EntityDTOMapper<E, D>,
     *               который выполняет преобразование из сущности в DTO.
     * @return объект DTO, соответствующий переданной сущности.
     * @throws IllegalArgumentException если переданный объект сущности равен null.
     */
    public <E, D> D transformFromEntityToDTO(E entity, EntityDTOMapper<E, D> mapper);

    /**
     * Метод transformFromDTOToEntity:
     * Преобразует объект передачи данных (DTO) в объект сущности (Entity).
     *
     * @param <E> тип сущности, которая будет возвращена (например, User, Task и т.д.).
     * @param <D> тип DTO, который будет преобразован (например, UserDTO, TaskDTO и т.д.).
     * @param dto объект DTO, который нужно преобразовать в сущность. Не может быть null.
     * @param mapper объект маппера, реализующий интерфейс EntityDTOMapper<E, D>,
     *               который выполняет преобразование из DTO в сущность.
     * @return объект сущности, соответствующий переданному DTO.
     * @throws IllegalArgumentException если переданный объект DTO равен null.
     */
    public <E, D> E transformFromDTOToEntity(D dto, EntityDTOMapper<E, D> mapper);
}
