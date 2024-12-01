package com.example.taskmanagmentsystemproject.models.transformer;

import com.example.taskmanagmentsystemproject.mapper.Transformer;
import com.example.taskmanagmentsystemproject.serviceDTO.EntityDTOMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

/**
 * Класс GenericTransformer:
 *
 * Универсальный сервис для преобразования между сущностями и DTO в приложении.
 * Основные функции:
 * - Преобразование сущностей в DTO.
 * - Преобразование DTO в сущности.
 *
 * Ключевые функции:
 * - Использование обобщенных типов для обеспечения универсальности.
 * - Применение мапперов для выполнения преобразований.
 * - Обработка исключений, возникающих при неправильных аргументах.
 *
 * Зависимости:
 * - Интерфейс EntityDTOMapper: для преобразования сущностей и DTO.
 *
 * Методы:
 * - {@link #transformFromEntityToDTO(Object, EntityDTOMapper)}: Преобразует сущность в DTO.
 * - {@link #transformFromDTOToEntity(Object, EntityDTOMapper)}: Преобразует DTO в сущность.
 *
 * Исключения:
 * - IllegalArgumentException: выбрасывается, если аргументы метода равны null.
 *
 * Аннотации:
 * - @Service: указывает, что класс является компонентом Spring.
 */
@Service
public class GenericTransformer implements Transformer
{

    /**
     * Метод transformFromEntityToDTO:
     * Преобразует сущность в объект DTO с использованием переданного маппера.
     *
     * @param <E> тип сущности.
     * @param <D> тип DTO.
     * @param entity объект сущности для преобразования.
     * @param mapper маппер, реализующий преобразование.
     * @return объект DTO.
     */
    @Override
    public <E, D> D transformFromEntityToDTO(E entity, EntityDTOMapper<E, D> mapper)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return mapper.toDTO(entity);
    }

    /**
     * Метод transformFromDTOToEntity:
     * Преобразует DTO в объект сущности с использованием переданного маппера.
     *
     * @param <E> тип сущности.
     * @param <D> тип DTO.
     * @param dto объект DTO для преобразования.
     * @param mapper маппер, реализующий преобразование.
     * @return объект сущности.
     */
    @Override
    public <E, D> E transformFromDTOToEntity(@Valid D dto, EntityDTOMapper<E, D> mapper)
    {
        if (dto == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        return mapper.toEntity(dto);
    }
}
