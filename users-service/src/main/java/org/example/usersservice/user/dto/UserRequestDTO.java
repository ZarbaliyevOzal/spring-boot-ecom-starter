package org.example.usersservice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRequestDTO {

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 100)
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 100)
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Size(max = 255)
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20)
    private String password;
}
