package org.example.usersservice.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.usersservice.user.dto.UserRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        authService.signUp(userRequestDTO);
        return ResponseEntity.ok().body(Map.of("message", "Successfully signed up"));
    }
}
