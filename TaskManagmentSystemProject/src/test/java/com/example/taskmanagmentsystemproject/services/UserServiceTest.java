package com.example.taskmanagmentsystemproject.services;

import com.example.taskmanagmentsystemproject.dto.UserDTO;
import com.example.taskmanagmentsystemproject.dto.UserRegisterDTO;
import com.example.taskmanagmentsystemproject.exceptions.customExeptions.PasswordSizeException;
import com.example.taskmanagmentsystemproject.mapper.UserMapper;
import com.example.taskmanagmentsystemproject.models.user.User;
import com.example.taskmanagmentsystemproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;
    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }

    @Test
    public void testGetUserById()
    {
        // Мокаем зависимость
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findUserById(1)).thenReturn(user);

        // Мокаем маппер
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com"); // Используем сеттер для установки email
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Вызываем метод
        UserDTO result = userService.getUserById(1);

        // Проверка
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testAuthenticate()
    {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        // Мокаем поведение зависимостей
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        // Вызываем метод
        User authenticatedUser = userService.authenticate("test@example.com", "password");

        // Проверка
        assertNotNull(authenticatedUser);
        assertEquals("test@example.com", authenticatedUser.getEmail());
    }

    @Test
    public void testGetUserByUserName()
    {
        User user = new User();
        user.setEmail("test@example.com");

        // Мокаем поведение зависимостей
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // Вызываем метод
        User foundUser = userService.getUserByUserName("test@example.com");

        // Проверка
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    public void testCheckValidDate()
    {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setPassword("1234"); // Пароль короче пяти символов

        assertThrows(PasswordSizeException.class,
                () -> userService.checkValidDate(userRegisterDTO));
    }

}


