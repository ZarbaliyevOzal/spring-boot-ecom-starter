package org.example.usersservice.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.usersservice.exception.EntityNotFoundException;
import org.example.usersservice.exception.FieldValidationException;
import org.example.usersservice.user.dto.UserRequestDTO;
import org.example.usersservice.user.dto.UserResponseDTO;
import org.example.usersservice.user.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        // save user
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    public void updateUser(Long id, @Valid UserUpdateDTO userUpdateDTO) {
        // find user
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }

        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }

        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        // update user
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setDeletedAt(Instant.now());
        userRepository.save(user);
    }

    public void restoreUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getDeletedAt() == null) return;
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    public UserResponseDTO findUser(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    public Page<UserResponseDTO> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByDeletedAtIsNull(pageable);

        return users.map(user -> new UserResponseDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()
            ));
    }
}
