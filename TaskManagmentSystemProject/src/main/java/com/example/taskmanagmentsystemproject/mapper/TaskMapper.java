package com.example.taskmanagmentsystemproject.mapper;

import com.example.taskmanagmentsystemproject.dto.TaskDTO;
import com.example.taskmanagmentsystemproject.models.task.Task;
import com.example.taskmanagmentsystemproject.serviceDTO.EntityDTOMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Интерфейс TaskMapper:
 *
 * Представляет собой маппер для преобразования сущности Task в DTO (Data Transfer Object) и обратно.
 * Используется библиотека MapStruct для автоматической генерации методов преобразования.
 * Этот маппер позволяет удобно преобразовывать объекты типа Task в TaskDTO и наоборот.
 *
 * Аннотации:
 * - @Mapper: Указывает, что это интерфейс для MapStruct. С параметром componentModel = "spring" интерфейс будет использоваться как компонент Spring.
 * - @Mapping: Указывает соответствие между полями сущности и полями DTO. Используется для явного указания, как должны быть скопированы данные между объектами.
 *
 * Методы:
 * - toEntity(TaskDTO dto): Преобразует объект типа TaskDTO в сущность Task.
 * - toDTO(Task entity): Преобразует сущность Task в объект типа TaskDTO.
 *
 * Связь с сущностью Task:
 * - Task - сущность, которая представляет задачу в системе управления задачами.
 *
 * Связь с DTO:
 * - TaskDTO - объект, представляющий данные задачи, которые будут передаваться через слои приложения.
 *
 * Пример использования:
 * - Использование этого маппера позволяет преобразовывать сущности в DTO для безопасной передачи данных в слои приложения, такие как сервисы и контроллеры.
 */
@Mapper(componentModel = "spring") // Компонент Spring для использования в Spring контексте
public interface TaskMapper extends EntityDTOMapper<Task, TaskDTO>
{
    /**
     * Преобразует объект типа TaskDTO в сущность Task.
     * MapStruct автоматически сгенерирует этот метод с использованием аннотации @Mapping.
     *
     * @param dto Объект TaskDTO, который будет преобразован в сущность Task.
     * @return Преобразованный объект Task.
     */
    @Mapping(source = "title", target = "title") // Преобразование поля "title" в поле "title"
    @Mapping(source = "status", target = "status") // Преобразование поля "status" в поле "status"
    @Mapping(source = "priority", target = "priority") // Преобразование поля "priority" в поле "priority"
    @Mapping(source = "author", target = "author") // Преобразование поля "author" в поле "author"
    Task toEntity(TaskDTO dto); // Преобразование TaskDTO в Task

    /**
     * Преобразует сущность Task в объект типа TaskDTO.
     * MapStruct автоматически сгенерирует этот метод с использованием аннотации @Mapping.
     *
     * @param entity Сущность Task, которая будет преобразована в TaskDTO.
     * @return Преобразованный объект TaskDTO.
     */
    @Mapping(source = "title", target = "title") // Преобразование поля "title" в поле "title"
    @Mapping(source = "status", target = "status") // Преобразование поля "status" в поле "status"
    @Mapping(source = "priority", target = "priority") // Преобразование поля "priority" в поле "priority"
    @Mapping(source = "author", target = "author") // Преобразование поля "author" в поле "author"
    TaskDTO toDTO(Task entity); // Преобразование Task в TaskDTO
}
