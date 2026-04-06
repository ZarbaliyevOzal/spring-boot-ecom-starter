package org.example.usersservice.user;

import org.example.usersservice.exception.EntityNotFoundException;
import org.example.usersservice.exception.FieldValidationException;
import org.example.usersservice.user.dto.UserRequestDTO;
import org.example.usersservice.user.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

    @Mock KeycloakService keycloakService;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO requestDTO;
    private User user;
    private User savedUser;
    private UserResponseDTO responseDTO;
    private UserRepresentation keycloakUser;

    @BeforeEach
    void setUp() {
        requestDTO = new UserRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("plainPassword");

        user = new User();
        user.setEmail("test@example.com");

        savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setKeycloakId("keycloak-id-123");

        responseDTO = new UserResponseDTO();
        responseDTO.setEmail("test@example.com");

        keycloakUser = new UserRepresentation();
        keycloakUser.setId("keycloak-id-123");
    }

    @Test
    void shouldCreateUser_whenRequestIsValid() {
        // given
        when(userRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(requestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(responseDTO);
        when(keycloakService.createUser(savedUser, requestDTO.getPassword())).thenReturn("keycloak-id-123");

        UserResponseDTO result = userService.createUser(requestDTO);

        // then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).existsByEmail(requestDTO.getEmail());
        verify(userMapper).toEntity(requestDTO);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(savedUser);
        verify(keycloakService).createUser(savedUser, requestDTO.getPassword());
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

    @Test
    void shouldSetDeletedAt_whenUserExists() {
        when(userRepository.findByIdAndDeletedAtIsNull(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(keycloakService.disableOrEnableUser(savedUser, false)).thenReturn(void);

        userService.deleteUser(user.getId());

        assertNotNull(user.getDeletedAt(), "deletedAt should be set");
        verify(userRepository, times(1)).save(user); // verify save was called
        verify(keycloakService, times(1)).disableOrEnableUser(savedUser, false);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByIdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.deleteUser(user.getId())
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any()); // verifies save was never called
    }
}