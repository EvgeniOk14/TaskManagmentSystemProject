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
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс TaskService:
 *
 * Реализует бизнес-логику для работы с задачами в приложении.
 * Отвечает за создание, получение, обновление, удаление задач, а также за фильтрацию задач по различным критериям.
 *
 * Ключевые функции:
 * - Работа с объектами Task и TaskDTO.
 * - Использование преобразователей и вспомогательных сервисов для преобразования данных.
 * - Интеграция с базой данных через TaskRepository.
 * - Атомарность (целостность или неделимость) операций за счет аннотации @Transactional.
 *
 * Зависимости:
 * - TaskRepository: для доступа к данным задач.
 * - GenericTransformer: для преобразования между Task и TaskDTO.
 * - TaskMapper: для трансформации (маппинга) полей между сущностью и DTO.
 * - UserHelperService: для работы с пользователями, такими как авторы и исполнители задач.
 *
 * Методы:
 * - {@link #createTask(TaskDTO)}: Создает новую задачу и сохраняет её в базе данных.
 * - {@link #getTaskById(Integer)}: Получает задачу по её идентификатору.
 * - {@link #updateTask(Integer, TaskDTO)}: Обновляет задачу по её идентификатору.
 * - {@link #deleteTask(Integer)}: Удаляет задачу по её идентификатору.
 * - {@link #getTasks(String, String, TaskStatus, TaskPriority, int, int)}: Получает список задач с фильтрацией и пагинацией.
 * - {@link #getTasksByAuthor(String)}: Получает список задач, созданных конкретным автором.
 * - {@link #getTasksByExecutor(String)}: Получает список задач, назначенных конкретному исполнителю.
 * - {@link #setTaskStatus(Integer id, TaskStatus status)}: Устанавливает статус задаче
 * - {@link #setTaskPriority(Integer id, TaskPriority priority)}: Устанавливает приоритет задаче
 * - {@link #assignExecutor(Integer executorId, Integer taskId)}: Устанавливает исполнителя задаче
 * - {@link #assignEAuthor(Integer authorId, Integer taskId)}: Устанавливает автора задаче
 *
 * Исключения:
 * - TaskNotSaveExсeption: кастомное исключение выбрасывается, если задача не может быть сохранена в базу данных.
 * - EmailIsNullExeption: кастомное исключение выбрасывается, если email текущего пользователя не найден.
 * - TaskNotFoundException: кастомное исключение выбрасывается, если задача не найдена в базе данных.
 * - TaskNotDeleteException: кастомное исключение выбрасывается, если удаление задачи невозможно.
 * - NotFoundArgumentException: кастомное исключение выбрасывается, если не найден определенный объект, необходимый для выполнения операции.
 * - FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
 * - IlligalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
 * Аннотации:
 * - @Service: указывает, что класс является сервисным компонентом в Spring.
 * - @Transactional: обеспечивает атомарность операций внутри методов.
 */
@Service
public class TaskService
{
    //region Fields
    private TaskRepository taskRepository;
    private GenericTransformer genericTransformer;
    private TaskMapper taskMapper;
    private UserHelperService userHelperService;
    //endRegion

    //region Constructor
    public TaskService(TaskRepository taskRepository, GenericTransformer genericTransformer, TaskMapper taskMapper, UserHelperService userHelperService)
    {
        this.taskRepository = taskRepository;
        this.genericTransformer = genericTransformer;
        this.taskMapper = taskMapper;
        this.userHelperService = userHelperService;
    }
    //endRegion

    //region Methods
    /**
     * Метод createTask: (Создание новой задачи)
     * Создает новую задачу и сохраняет её в базе данных.
     * @param taskDTO DTO объект задачи, содержащий данные для создания задачи.
     * @return объект Task, представляющий созданную задачу.
     * @exception  TaskNotSaveExсeption - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не может быть сохранена в базу данных.
     * @exception  EmailIsNullExeption - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если email текущего пользователя не найден (маловероятный случай).
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return TaskDTO - объект класса TaskDTO
     */
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public TaskDTO createTask(TaskDTO taskDTO)
    {
        if (taskDTO == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент taskDTO! taskDTO равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Извлекаем текущую аутентификацию пользователя.

            String email = authentication.getName(); // Получаем email текущего пользователя.
            if (email == null) // если email равен нулю, то:
            {
                throw new EmailIsNullExeption("Email пользователя не найден в контексте безопасности."); // выбрасываем исключение
            }

            User userAuthorOfTask = userHelperService.getAuthorByEmail(email); // Получаем автора задачи по email из базы данных.

            Task task = genericTransformer.transformFromDTOToEntity(taskDTO, taskMapper);  // Преобразуем DTO в сущность задачи.

            task.setAuthor(userAuthorOfTask); // Устанавливаем автора задачи.

            taskRepository.save(task); // Сохраняем задачу в базе данных.

            taskDTO = genericTransformer.transformFromEntityToDTO(task, taskMapper); // трансформация Task в TaskDTO

            return taskDTO; // Возвращаем созданную задачу.
        }
        catch (TaskNotSaveExсeption ex) // Обрабатываем ошибки при работе с базой данных.
        {
            throw new TaskNotSaveExсeption("Не удалось сохранить задачу в базу данных.");
        }
        catch (EmailIsNullExeption ex) // Повторно выбрасываем исключение для обработки на уровне контроллера.
        {
            throw ex;
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод getTaskById:
     * Находит задачу по переданому id
     * @param id - идентификационный номер
     * @return task - объект Task (задача)
     * @exception TaskNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return TaskDTO - объект класса TaskDTO
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public TaskDTO getTaskById(Integer id)
    {
        if (id == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Task task = taskRepository.getById(id); // получаем задачу по её id  из базы данных
            TaskDTO foundTaskDTO = genericTransformer.transformFromEntityToDTO(task, taskMapper); // трансформация Task в TaskDTO
            return foundTaskDTO;  // возвращаем найденную задачу в формате TaskDTO
        }
        catch (TaskNotFoundException ex) // обрабатываем кастомное исключение на глобальном уровне, в случае, если задача не найдена
        {
           throw  ex;
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод updateTask:
     * Обновляет задачу, найденную по её id.
     * @param id - идентификационный номер задачи
     * @param taskDTO - объект TaskDTO для передачи задачи пользователю
     * @exception TaskNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не может быть найдена в базе данных
     * @exception TaskNotSaveExсeption - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не может быть сохранена в базу данных.
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception AuthentificationGetContextException - кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, невозможности извлечения данных из контекста безопасности
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return TaskDTO - объект TaskDTO для передачи обновлённой задачи вместе с её обновлёнными комментариями пользователю
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public TaskDTO updateTask(Integer id, TaskDTO taskDTO)
    {
        if (id == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        if (taskDTO == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент taskDTO! taskDTO равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            // Извлекаем текущий объект аутентификации из контекста безопасности (SecurityContext).
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // получаем объект Authentication, представляющий текущего аутентифицированного пользователя.
            String currentUserEmail =  authentication.getName(); // Получаем имя (или идентификатор) текущего пользователя из объекта Authentication.
            User currentUser = userHelperService.getUserByEmail(currentUserEmail); // получаем объект пользователя (User) по его email.

            Task updateTask = taskRepository.getTaskById(id); // получение задачи по её id
            updateTask.setTitle(taskDTO.getTitle()); // устанавливаем задаче заголовок Title
            updateTask.setDescription(taskDTO.getDescription()); // устанавливаем задаче описание Description
            updateTask.setStatus(taskDTO.getStatus()); // устанавливаем задаче статус Status
            updateTask.setPriority(taskDTO.getPriority()); // устанавливаем задаче приоритет Priority
            //updateTask.setAuthor(taskDTO.getAuthor()); // устанавливаем задаче автора задачи Author
            updateTask.setAuthor(currentUser); // меняем автора задачи на текущего пользователя
            updateTask.setExecutor(taskDTO.getExecutor()); // устанавливаем задаче исполнителя задачи Executor

            // Преобразуем List<CommentDTO> в List<Comment> для сохранения в сущности Task:

            List<Comment> updatedComments = new ArrayList<>();  // Инициализация списка обновлённых комментариев как пустого, если он равен null

            if (taskDTO.getListOfComments() != null) // если список комментариев не пустой, то:
            {
                for (CommentDTO commentDTO : taskDTO.getListOfComments()) // идём по списку комментариев
                {
                    Comment comment = new Comment(); // создаём новый комментарий

                    comment.setId(commentDTO.getId()); // устанавливаем id коментария commentDTO
                    comment.setText(commentDTO.getText()); // устанавливаем текст комментария commentDTO
                    comment.setTask(updateTask); // Устанавливаем задачу для комментария commentDTO
                    comment.setUser(commentDTO.getUser()); // Устанавливаем пользователя для комментария

                    updatedComments.add(comment); // добавляем в список обнавлённых комментариев - созданный комментарий comment
                }
            }

            updateTask.setListOfComments(updatedComments); // устанавливаем обновляемой задаче updateTask - список с обновлёнными комментариями updatedComments

            taskRepository.save(updateTask); // сохраняем обновлённую задачу updateTask в базе данных

            return genericTransformer.transformFromEntityToDTO(updateTask, taskMapper); // возвращаем обновлённую задачу, с обновлёнными комментариями к этой задачи, в формате TaskDTO для дальнейшего ответа пользователю
        }
        catch (TaskNotFoundException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
        catch (TaskNotSaveExсeption ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если если задача не сохранена в базе данных
        {
          throw ex;
        }
        catch (FailTransformException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
        catch (AuthentificationGetContextException ex) // кастомное исключение, обрабатываем на глобальном уровне исключение, в случае, невозможности извлечения данных из контекста безопасности
        {
            throw ex;
        }
    }

    /**
     * Метод deleteTask:
     * Удаляет задачу по заданному id
     * @param id - идентификационный номер задачи
     * @exception TaskNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не может быть найдена в базе данных
     * @exception TaskNotDeleteException -кастомное исключение, обрабатываемое на глобальном уровне, в случае, если удаление задачи невозможно по каки-либо причинам из базе данных
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public void deleteTask(Integer id)
    {
        if (id == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            taskRepository.deleteById(id); // // удаление задачи из базы данных по её id
        }
        catch (TaskNotFoundException ex) // обрабатываем на глобальном уровне исключение, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
        catch (TaskNotDeleteException ex) // обрабатываем на глобальном уровне исключение, в случае, если удаление задачи невозможно по каки-либо причинам из базе данных
        {
            throw ex;
        }
    }

    /**
     * Метод getTasks:
     *
     * Получает список задач пользователя, где он одновременно является автором и исполнителем,
     * с применением фильтрации по статусу, приоритету и пагинации.
     *
     * @param authorEmail   email автора задачи (может быть null для игнорирования фильтра).
     * @param executorEmail email исполнителя задачи (может быть null для игнорирования фильтра).
     * @param status        статус задачи (может быть null для игнорирования фильтра).
     * @param priority      приоритет задачи (может быть null для игнорирования фильтра).
     * @param page          номер страницы для пагинации (начиная с 0).
     * @param size          количество записей на странице для пагинации.
     * @return Page<TaskDTO> список задач, соответствующих фильтрам и разбитый по страницам.
     * @exception  TaskNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задачи не найдены.
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @return Page<TaskDTO> - возвращает страницу с задачами
     */
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public Page<TaskDTO> getTasks(String authorEmail, String executorEmail, TaskStatus status, TaskPriority priority, int page, int size) {
        try
        {
            Pageable pageable = PageRequest.of(page, size); // Создаем объект пагинации с указанным номером страницы и количеством записей на странице

            Page<Task> tasks = taskRepository.findTasksByFilters(authorEmail, executorEmail, status, priority, pageable); // Получаем список задач с фильтрацией и подгрузкой авторов, исполнителей и комментариев

            // Преобразуем Page<Task> в Page<TaskDTO> и включаем комментарии:

            List<TaskDTO> taskDTOList = tasks.getContent().stream() // Берем содержимое страницы задач
                    .map(task -> {
                        TaskDTO taskDTO = genericTransformer.transformFromEntityToDTO(task, taskMapper); // Преобразуем задачу в DTO

                        // Преобразуем комментарии задачи в DTO:
                        List<CommentDTO> commentDTOList = task.getListOfComments().stream() // Преобразуем список комментариев задачи
                                .map(comment -> new CommentDTO(comment.getId(), comment.getText(), comment.getTask(), comment.getUser())) // Создаем объект CommentDTO
                                .collect(Collectors.toList()); // Собираем все CommentDTO в список

                        taskDTO.setListOfComments(commentDTOList); // Устанавливаем список комментариев в объект TaskDTO

                        return taskDTO; // Возвращаем преобразованный объект TaskDTO
                    })
                    .collect(Collectors.toList()); // Собираем все TaskDTO в список

            // Возвращаем Page<TaskDTO>
            return new PageImpl<>(taskDTOList, pageable, tasks.getTotalElements()); // Создаем и возвращаем страницу с задачами
        }
        catch (TaskNotFoundException ex) // Если задачи не найдены, выбрасываем исключение для обработки на глобальном уровне сервиса.
        {
            throw ex; // Если задачи не найдены, выбрасываем кастомное исключение
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex; // Если произошла ошибка при трансформации объекта, выбрасываем кастомное исключение
        }
    }

    /**
     * Метод getTasksByAuthor:
     * Получает список задач по их автору
     * @param authorEmail - имя (mail) автора создавшую задачу
     * @exception NotFoundArgumentException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если нахождение какого-либо аргумента у какого-либо объекта невозможно по каки-либо причинам
     * @exception TaskNotFoundException -   кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return List<TaskDTO> - список задач, у которых автор совпадает с переданным параметром authorEmail
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public List<TaskDTO> getTasksByAuthor(String authorEmail)
    {
        if (authorEmail == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент authorEmail! authorEmail равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            User author = userHelperService.getAuthorByEmail(authorEmail); // находим аргумент author (автор) у объекта класса User
            List<Task> tasks = taskRepository.findByAuthor(author); // находим все задачи с данным автором
            return tasks.stream()
                    .map(task -> taskMapper.toDTO(task))
                    .collect(Collectors.toList()); // возвращаем список найденных задач из базы данных
        }
        catch (NotFoundArgumentException ex) // обрабатываем на глобальном уровне исключение, в случае, если нахождение какого-либо аргумента у какого-либо объекта невозможно по каки-либо причинам
        {
            throw ex;
        }
        catch (TaskNotFoundException ex) // обрабатываем на глобальном уровне исключение, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод getTasksByExecutor:
     * Получает список задач по их исполнителю.
     * @param executorEmail - имя (mail) исполнителя назначенного на данную задачу
     * @exception NotFoundArgumentException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если нахождение какого-либо аргумента у какого-либо объекта невозможно по каки-либо причинам
     * @exception TaskNotFoundException -   кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return List<TaskDTO> - список задач, у которых автор совпадает с переданным параметром authorEmail
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public List<TaskDTO> getTasksByExecutor(String executorEmail)
    {
        if (executorEmail == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент executorEmail! executorEmail равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }

        try
        {
            User executor = userHelperService.getExecutorByEmail(executorEmail); // получаем исполнителя по его email через сервис
            List<Task> tasks = taskRepository.findByExecutor(executor); // получаем список задач у данного исполнителя

            return tasks.stream()
                    .map(task -> taskMapper.toDTO(task))
                    .collect(Collectors.toList());
        }
        catch (NotFoundArgumentException ex) // обрабатываем на глобальном уровне исключение, в случае, если нахождение какого-либо аргумента у какого-либо объекта невозможно по каки-либо причинам
        {
            throw ex;
        }
        catch (TaskNotFoundException ex) // обрабатываем на глобальном уровне исключение, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод getTasksByAuthorAndExecutor:
     * Получает список задач по их автору и исполнителю.
     * @param email - имя (mail) автора и исполнителя, который одновременно был назначен на данную задачу
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception TaskNotFoundException -   кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return List<TaskDTO> - список задач, у которых автор и исполнитель совпадают с переданным параметром email
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public List<TaskDTO> getTasksByAuthorAndExecutor(String email)
    {
        if (email == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент email! email равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            List<Task> tasks = taskRepository.findByAuthorEmailAndExecutorEmail(email, email); // получаем задачи по email (имени) пользователя

            return tasks.stream().map(task -> genericTransformer.transformFromEntityToDTO(task, taskMapper)).collect(Collectors.toList()); // трансформируем задачи в объекты TransDTO
        }
        catch (TaskNotFoundException ex) // обрабатываем на глобальном уровне исключение, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод setTaskStatus:
     * Предназначен для установление статуса конкретной задаче(по id задачи):
     * @param id - идентификационный номер задачи в базе данных
     * @param status - статус задачи
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception TaskNotFoundException -  кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return taskDTO - возвращаем трансформированную задачу в объект TaskDTO
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public TaskDTO setTaskStatus(Integer id, TaskStatus status)
    {
        if (id == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        if (status == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент status! status равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Task task = taskRepository.getTaskById(id); // находим задачу по её id
            task.setStatus(status); // устанавливаем задаче статус
            TaskDTO taskDTO = genericTransformer.transformFromEntityToDTO(task, taskMapper); // трансформация задачи в объект TaskDTO
            return taskDTO; // возврат трансформированной задачи, т.е. объекта TaskDTO

        }
        catch (TaskNotFoundException ex) //  обрабатываем на глобальном уровне исключение, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод setTaskPriority
     * Предназначен для установление приоритета конкретной задаче(по id задачи):
     * @param id - идентификационный номер задачи в базе данных
     * @param priority - приоритет задачи
     * @exception FailTransformException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если невозможна трансформация одного объекта в другой
     * @exception TaskNotFoundException -  кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * @return taskDTO - возвращаем трансформированную задачу в объект TaskDTO
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public TaskDTO setTaskPriority(Integer id, TaskPriority priority)
    {
        if (id == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент id! id равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        if (priority == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент priority! priority равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Task task = taskRepository.getTaskById(id); // находим задачу по её id
            task.setPriority(priority); // устанавливаем задаче приоритет
            TaskDTO taskDTO = genericTransformer.transformFromEntityToDTO(task, taskMapper); // трансформация задачи в объект TaskDTO
            return taskDTO; // возврат трансформированной задачи, т.е. объекта TaskDTO

        }
        catch (TaskNotFoundException ex) //  обрабатываем на глобальном уровне исключение, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
        catch (FailTransformException ex) // обрабатываем на глобальном уровне исключение, в случае, если невозможна трансформация одного объекта в другой
        {
            throw ex;
        }
    }

    /**
     * Метод assignExecutor:
     * Назначает исполнителя задаче
     * @param executorId - идентификационный номер пользователя (исполнителя)
     * @param taskId - идентификационный номер задачи
     * @exception UserNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если пользователь не найден в базе данных
     * @exception TaskNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public void assignExecutor(Integer executorId, Integer taskId)
    {
        if (executorId == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент executorId! executorId равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        if (taskId == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент taskId! taskId равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Task task = taskRepository.getTaskById(taskId); // получаем задачу

            User executor = userHelperService.getUserById(executorId); // получаем пользователя

            task.setExecutor(executor); // устанавливаем задаче исполнителя
        }
        catch (UserNotFoundException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если пользователь не найден в базе данных
        {
            throw ex;
        }
        catch (TaskNotFoundException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
    }

    /**
     * Метод assignEAuthor:
     * Назначает автора задаче
     * @param authorId - идентификационный номер пользователя (автора)
     * @param taskId - идентификационный номер задачи
     * @exception UserNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если пользователь не найден в базе данных
     * @exception TaskNotFoundException - кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
     * @exception IllegalArgumentException - системное исключение обрабатывающее случай, когда был некорректно передан аргумент
     * **/
    @Transactional // Обеспечиваем атомарность всех операций в методе.
    public void assignEAuthor(Integer authorId, Integer taskId)
    {
        if (authorId == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент executorId! executorId равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        if (taskId == null) // если переданный аргумент равен null, то:
        {
            throw new IllegalArgumentException("Некорректный аргумент taskId! taskId равен null."); // Выбрасываем исключение обрабатывающее случай, когда был некорректно передан аргумент
        }
        try
        {
            Task task = taskRepository.getTaskById(taskId); // получаем задачу

            User author = userHelperService.getUserById(authorId); // получаем пользователя

            task.setAuthor(author); // устанавливаем задаче автора
        }
        catch (UserNotFoundException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если пользователь не найден в базе данных
        {
            throw ex;
        }
        catch (TaskNotFoundException ex) // кастомное исключение, обрабатываемое на глобальном уровне, в случае, если задача не найдена в базе данных
        {
            throw ex;
        }
    }
    //endRegion
}
