package org.example.usersservice.handler;

import org.example.usersservice.exception.EntityNotFoundException;
import org.example.usersservice.exception.FieldValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // helper to convert camelCase -> snake_case
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(toSnakeCase(error.getField()), error.getDefaultMessage())
        );
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bad request");
        response.put("errors", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<Map<String, Object>> handleFieldValidation(FieldValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bad request");
        response.put("errors", ex.getErrors());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
