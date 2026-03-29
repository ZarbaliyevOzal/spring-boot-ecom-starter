package org.example.usersservice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.usersservice.validation.annotation.FieldMatch;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldMatch(field = "password",
        fieldMatch = "passwordConfirmation",
        message = "Passwords do not match")
public class UserUpdateDTO {

    @NotBlank
    @Size(max = 100)
    @JsonProperty("first_name")
    private String firstName;

    @Size(max = 100)
    @JsonProperty("last_name")
    private String lastName;

    @Size(min = 8, max = 20)
    private String password;

    @Size(min = 8, max = 20)
    @JsonProperty("password_confirmation")
    private String passwordConfirmation;
}
