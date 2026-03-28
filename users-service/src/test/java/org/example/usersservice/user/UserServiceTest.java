package org.example.usersservice.user;

import org.example.usersservice.exception.FieldValidationException;
import org.example.usersservice.user.dto.UserRequestDTO;
import org.example.usersservice.user.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO requestDTO;
    private User user;
    private User savedUser;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new UserRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("plainPassword");

        user = new User();
        user.setEmail("test@example.com");

        savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword");

        responseDTO = new UserResponseDTO();
        responseDTO.setEmail("test@example.com");
    }

    @Test
    void shouldCreateUser_whenRequestIsValid() {
        // given
        when(userRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(requestDTO)).thenReturn(user);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(responseDTO);

        // when
        UserResponseDTO result = userService.createUser(requestDTO);

        // then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).existsByEmail(requestDTO.getEmail());
        verify(userMapper).toEntity(requestDTO);
        verify(passwordEncoder).encode(requestDTO.getPassword());
        verify(userRepository).save(user);
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    void shouldThrowFieldValidationException_whenEmailAlreadyExists() {
        // given
        when(userRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        // when
        FieldValidationException exception = assertThrows(
                FieldValidationException.class,
                () -> userService.createUser(requestDTO)
        );

        // then
        assertTrue(exception.getErrors().containsKey("email"));
        assertEquals("Email already in use", exception.getErrors().get("email"));

        verify(userRepository).existsByEmail(requestDTO.getEmail());
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder);
    }
}