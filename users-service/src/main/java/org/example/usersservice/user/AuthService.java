package org.example.usersservice.user;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.usersservice.exception.FieldValidationException;
import org.example.usersservice.user.dto.UserRequestDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;

    @Transactional
    public void signUp(@Valid UserRequestDTO userRequestDTO) {
        // check user exists with email address
        if (userRepository.existsByEmail(userRequestDTO.getEmail()))
            throw new FieldValidationException(Map.of("email", "Email already in use"));

        // creat user entity
        User user = userMapper.toEntity(userRequestDTO);

        // save user entity
        userRepository.save(user);

        // create keycloak user
        String keycloakUserId = keycloakService.createUser(user, userRequestDTO.getPassword());

        user.setKeycloakId(keycloakUserId);

        userRepository.save(user);

        // todo: set user keycloak_id

        // todo: update user

        // todo: create queue to send keycloak verify email
    }
}
