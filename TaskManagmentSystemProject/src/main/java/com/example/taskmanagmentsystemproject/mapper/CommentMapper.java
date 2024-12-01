package com.example.taskmanagmentsystemproject.mapper;

import com.example.taskmanagmentsystemproject.dto.CommentDTO;
import com.example.taskmanagmentsystemproject.models.comment.Comment;
import com.example.taskmanagmentsystemproject.serviceDTO.EntityDTOMapper;
import org.mapstruct.Mapper;

/**
 * Интерфейс CommentMapper:
 *
 * Представляет собой маппер для преобразования сущности Comment в DTO (Data Transfer Object) и обратно.
 * Используется библиотека MapStruct для автоматической генерации методов преобразования.
 * Этот маппер позволяет удобно преобразовывать объекты типа Comment в CommentDTO и наоборот.
 *
 * Аннотации:
 * - @Mapper: Указывает, что это интерфейс для MapStruct. С параметром componentModel = "spring" интерфейс будет использоваться как компонент Spring.
 *
 * Методы:
 * - toDTO(Comment entity): Преобразует сущность Comment в объект типа CommentDTO.
 * - toEntity(CommentDTO dto): Преобразует объект типа CommentDTO в сущность Comment.
 *
 * Связь с сущностью Comment:
 * - Comment - сущность, которая представляет комментарий в системе управления задачами.
 *
 * Связь с DTO:
 * - CommentDTO - объект, представляющий данные комментария, которые будут передаваться через слои приложения.
 *
 * Пример использования:
 * - Использование этого маппера позволяет преобразовывать сущности в DTO для безопасной передачи данных в слои приложения, такие как сервисы и контроллеры.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityDTOMapper<Comment, CommentDTO>
{
    // Автоматическое преобразование Comment <-> CommentDTO
    // MapStruct сгенерирует методы:
    // - toDTO(Comment entity) -> CommentDTO
    // - toEntity(CommentDTO dto) -> Task

    /**
     * Преобразует сущность Comment в объект типа CommentDTO.
     * MapStruct автоматически сгенерирует этот метод.
     *
     * @param entity Сущность Comment, которая будет преобразована в CommentDTO.
     * @return Преобразованный объект CommentDTO.
     */
    // CommentDTO toDTO(Comment entity); // Преобразование Comment в CommentDTO

    /**
     * Преобразует объект типа CommentDTO в сущность Comment.
     * MapStruct автоматически сгенерирует этот метод.
     *
     * @param dto Объект CommentDTO, который будет преобразован в сущность Comment.
     * @return Преобразованный объект Comment.
     */
    // Comment toEntity(CommentDTO dto); // Преобразование CommentDTO в Comment
}
