package org.example.order_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
public class FieldValidationException extends RuntimeException {

    private final Map<String, String> errors;

}
