package org.example.usersservice.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
