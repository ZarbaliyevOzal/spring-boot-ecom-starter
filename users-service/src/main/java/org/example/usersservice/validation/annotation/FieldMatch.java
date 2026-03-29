package org.example.usersservice.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.usersservice.validation.validator.FieldMatchValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMatch {

    String message() default "Fields do not match";

    String field();
    String fieldMatch();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
