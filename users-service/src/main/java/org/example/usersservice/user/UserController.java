package org.example.usersservice.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.usersservice.user.dto.UserRequestDTO;
import org.example.usersservice.user.dto.UserResponseDTO;
import org.example.usersservice.user.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER_MANAGE')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER_MANAGE')")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Long id,
                                                          @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok().body(Map.of("message", "User successfully updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER_MANAGE')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body(Map.of("message", "User successfully deleted"));
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER_MANAGE')")
    public ResponseEntity<Map<String, String>> restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return ResponseEntity.ok().body(Map.of("message", "User successfully restored"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER_MANAGE','USER_VIEW')")
    public ResponseEntity<UserResponseDTO> findUser(@PathVariable Long id) {
        UserResponseDTO user = userService.findUser(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER_MANAGE','USER_VIEW')")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        page = page <= 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UserResponseDTO> users = userService.getUsers(pageable);
        return ResponseEntity.ok().body(users);
    }
}
