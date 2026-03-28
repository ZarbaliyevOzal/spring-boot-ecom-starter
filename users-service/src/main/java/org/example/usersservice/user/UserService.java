package org.example.usersservice.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.usersservice.exception.FieldValidationException;
import org.example.usersservice.user.dto.UserRequestDTO;
import org.example.usersservice.user.dto.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(@Valid UserRequestDTO userRequestDTO) {
        Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            errors.put("email", "Email already in use");
            throw new FieldValidationException(errors);
        }

        User user = userMapper.toEntity(userRequestDTO);
        // hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // save user
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }
}
