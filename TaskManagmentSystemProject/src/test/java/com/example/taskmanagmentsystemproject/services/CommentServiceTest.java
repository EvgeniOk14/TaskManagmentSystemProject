package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.dto.CommentDTO;
import com.example.taskmanagmentsystemproject.mapper.CommentMapper;
import com.example.taskmanagmentsystemproject.models.comment.Comment;
import com.example.taskmanagmentsystemproject.models.task.Task;
import com.example.taskmanagmentsystemproject.models.transformer.GenericTransformer;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.repository.CommentRepository;
import com.example.taskmanagmentsystemproject.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserHelperService userHelperService;

    @Mock
    private GenericTransformer genericTransformer;

    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private TaskService taskService; // Здесь предполагается, что TaskService содержит метод createNewComment

    @Test
    void createNewComment_shouldCreateAndReturnCommentDTO_whenInputIsValid() {
        // Arrange
        Integer taskId = 1;
        String userEmail = "user@example.com";

        CommentDTO inputCommentDTO = new CommentDTO();
        inputCommentDTO.setText("New Comment");

        Comment mockComment = new Comment();
        mockComment.setId(1);
        mockComment.setText("New Comment");

        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail(userEmail);

        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setListOfComments(new ArrayList<>());

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn(userEmail);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        when(userHelperService.getUserByEmail(userEmail)).thenReturn(mockUser);
        when(taskRepository.getTaskById(taskId)).thenReturn(mockTask);
        when(genericTransformer.transformFromDTOToEntity(inputCommentDTO, commentMapper)).thenReturn(mockComment);
        when(genericTransformer.transformFromEntityToDTO(mockComment, commentMapper)).thenReturn(inputCommentDTO);
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        // Act
        CommentDTO result = commentService.createNewComment(taskId, inputCommentDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Comment", result.getText());
        assertEquals(1, mockTask.getListOfComments().size());
        assertEquals(mockComment, mockTask.getListOfComments().get(0));

        verify(commentRepository, times(1)).save(mockComment);
        verify(taskRepository, times(1)).save(mockTask);
        verify(userHelperService, times(1)).getUserByEmail(userEmail);
        verify(taskRepository, times(1)).getTaskById(taskId);

        SecurityContextHolder.clearContext();
    }


}

