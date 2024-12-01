package com.example.taskmanagmentsystemproject.serviceDTO;

import jakarta.validation.Valid;

/**
 * Универсальный интерфейс EntityDTOMapper:
 *
 * Предназначен для преобразования между сущностями (Entity) и объектами передачи данных (DTO).
 * Это удобный способ разделения слоев приложения (например, сервисов и контроллеров).
 *
 * @param <E> Тип сущности (Entity), например, User, Task.
 * @param <D> Тип объекта DTO (Data Transfer Object), например, UserDTO, TaskDTO.
 */
public interface EntityDTOMapper<E, D>
{
    /**
     * Преобразует сущность в объект DTO.
     *
     * @param entity объект сущности, который нужно преобразовать.
     * @return объект DTO, соответствующий переданной сущности.
     */
    D toDTO(E entity);

    /**
     * Преобразует объект DTO в сущность.
     *
     * @param dto объект DTO, который нужно преобразовать.
     * @return объект сущности, соответствующий переданному DTO.
     */
    E toEntity(@Valid D dto);
}
