package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.dto.CommentDTO;
import com.example.taskmanagmentsystemproject.dto.TaskDTO;
import com.example.taskmanagmentsystemproject.exceptions.customExeptions.*;
import com.example.taskmanagmentsystemproject.mapper.TaskMapper;
import com.example.taskmanagmentsystemproject.models.comment.Comment;
import com.example.taskmanagmentsystemproject.models.task.Task;
import com.example.taskmanagmentsystemproject.models.task.TaskPriority;
import com.example.taskmanagmentsystemproject.models.task.TaskStatus;
import com.example.taskmanagmentsystemproject.models.transformer.GenericTransformer;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest
{

    @InjectMocks
    private TaskService taskService; // Сервис, содержащий метод createTask

    @Mock
    private UserHelperService userHelperService; // Мокируем зависимость UserHelperService

    @Mock
    private TaskRepository taskRepository; // Мокируем зависимость TaskRepository

    @Mock
    private GenericTransformer genericTransformer; // Мокируем зависимость GenericTransformer

    @Mock
    private TaskMapper taskMapper; // Мокируем зависимость TaskMapper

    @Mock
    private Authentication authentication; // Мокируем объект Authentication

    @Mock
    private User user; // Мокируем объект пользователя

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков

        // Настройка SecurityContextHolder для имитации аутентифицированного пользователя
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void createTask_Success() {
        // Подготовка данных
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Task Description");

        // Мокируем возвращаемые значения
        when(authentication.getName()).thenReturn("test@example.com");
        when(userHelperService.getAuthorByEmail("test@example.com")).thenReturn(user);
        when(genericTransformer.transformFromDTOToEntity(taskDTO, taskMapper)).thenReturn(new Task());
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());
        when(genericTransformer.transformFromEntityToDTO(any(Task.class), eq(taskMapper))).thenReturn(taskDTO); // Используем eq для второго аргумента

        // Вызов метода
        TaskDTO result = taskService.createTask(taskDTO);

        // Проверки
        assertNotNull(result);
        assertEquals(taskDTO.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class)); // Проверяем, что save вызван один раз
    }

    @Test
    public void createTask_EmailIsNullException() {
        // Подготовка данных
        TaskDTO taskDTO = new TaskDTO();

        // Мокируем аутентификацию, чтобы email был null
        when(authentication.getName()).thenReturn(null);

        // Вызов метода и проверка исключения
        assertThrows(EmailIsNullExeption.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    public void createTask_TaskNotSaveException() {
        // Подготовка данных
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Task Description");

        // Мокируем аутентификацию
        when(authentication.getName()).thenReturn("test@example.com");
        when(userHelperService.getAuthorByEmail("test@example.com")).thenReturn(user);
        when(genericTransformer.transformFromDTOToEntity(taskDTO, taskMapper)).thenReturn(new Task());
        when(taskRepository.save(any(Task.class))).thenThrow(TaskNotSaveExсeption.class);

        // Вызов метода и проверка исключения
        assertThrows(TaskNotSaveExсeption.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    public void createTask_IllegalArgumentException() {
        // Вызов метода с null объектом и проверка исключения
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(null));
    }

    @Test
    public void createTask_FailTransformException() {
        // Подготовка данных
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");

        // Мокируем аутентификацию
        when(authentication.getName()).thenReturn("test@example.com");
        when(userHelperService.getAuthorByEmail("test@example.com")).thenReturn(user);
        when(genericTransformer.transformFromDTOToEntity(taskDTO, taskMapper)).thenThrow(FailTransformException.class);

        // Вызов метода и проверка исключения
        assertThrows(FailTransformException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    void deleteTask_shouldThrowIllegalArgumentException_whenIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.deleteTask(null)
        );
        assertEquals("Некорректный аргумент id! id равен null.", exception.getMessage());
    }

    @Test
    void deleteTask_shouldCallDeleteById_whenIdIsValid() {
        // Arrange
        Integer validId = 1;

        // Act
        taskService.deleteTask(validId);

        // Assert
        verify(taskRepository, times(1)).deleteById(validId);
    }

    @Test
    void deleteTask_shouldThrowTaskNotFoundException_whenTaskNotFound() {
        // Arrange
        Integer taskId = 2;
        doThrow(new TaskNotFoundException("Task not found"))
                .when(taskRepository)
                .deleteById(taskId);

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(taskId)
        );
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void deleteTask_shouldThrowTaskNotDeleteException_whenDeletionFails() {
        // Arrange
        Integer taskId = 3;
        doThrow(new TaskNotDeleteException("Task cannot be deleted"))
                .when(taskRepository)
                .deleteById(taskId);

        // Act & Assert
        TaskNotDeleteException exception = assertThrows(
                TaskNotDeleteException.class,
                () -> taskService.deleteTask(taskId)
        );
        assertEquals("Task cannot be deleted", exception.getMessage());
    }

    @Test
    void getTasks_shouldReturnPageOfTaskDTO_whenTasksAreFound()
    {
        // Arrange
        String authorEmail = "author@example.com";
        String executorEmail = "executor@example.com";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        TaskPriority priority = TaskPriority.HIGH;
        int page = 0;
        int size = 5;

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

        // Mock task data
        Task mockTask = new Task();
        mockTask.setId(1);
        mockTask.setListOfComments(List.of(new Comment(1, "Test comment", mockTask, null)));

        Page<Task> mockTasksPage = new PageImpl<>(List.of(mockTask), pageable, 1);

        // Mock TaskDTO
        TaskDTO mockTaskDTO = new TaskDTO();
        mockTaskDTO.setId(1);
        mockTaskDTO.setListOfComments(List.of(new CommentDTO(1, "Test comment", mockTask, null)));

        // Mock repository and transformer behavior
        when(taskRepository.findTasksByFilters(authorEmail, executorEmail, status, priority, pageable))
                .thenReturn(mockTasksPage);
        when(genericTransformer.transformFromEntityToDTO(mockTask, taskMapper))
                .thenReturn(mockTaskDTO);

        // Act
        Page<TaskDTO> result = taskService.getTasks(authorEmail, executorEmail, status, priority, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getContent().get(0).getId());
        verify(taskRepository, times(1))
                .findTasksByFilters(authorEmail, executorEmail, status, priority, pageable);
    }

    @Test
    void getTasks_shouldThrowTaskNotFoundException_whenNoTasksAreFound() {
        // Arrange
        String authorEmail = "author@example.com";
        String executorEmail = "executor@example.com";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        TaskPriority priority = TaskPriority.HIGH;
        int page = 0;
        int size = 5;

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

        // Mock repository to throw TaskNotFoundException
        when(taskRepository.findTasksByFilters(authorEmail, executorEmail, status, priority, pageable))
                .thenThrow(new TaskNotFoundException("Tasks not found"));

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTasks(authorEmail, executorEmail, status, priority, page, size)
        );

        assertEquals("Tasks not found", exception.getMessage());
    }

    @Test
    void getTasks_shouldThrowFailTransformException_whenTransformationFails() {
        // Arrange
        String authorEmail = "author@example.com";
        String executorEmail = "executor@example.com";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        TaskPriority priority = TaskPriority.HIGH;
        int page = 0;
        int size = 5;

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

        // Mock task data
        Task mockTask = new Task();
        mockTask.setId(1);

        Page<Task> mockTasksPage = new PageImpl<>(List.of(mockTask), pageable, 1);

        // Mock repository behavior
        when(taskRepository.findTasksByFilters(authorEmail, executorEmail, status, priority, pageable))
                .thenReturn(mockTasksPage);

        // Mock transformer to throw exception
        when(genericTransformer.transformFromEntityToDTO(mockTask, taskMapper))
                .thenThrow(new FailTransformException("Transformation failed"));

        // Act & Assert
        FailTransformException exception = assertThrows(
                FailTransformException.class,
                () -> taskService.getTasks(authorEmail, executorEmail, status, priority, page, size)
        );

        assertEquals("Transformation failed", exception.getMessage());
    }
}
