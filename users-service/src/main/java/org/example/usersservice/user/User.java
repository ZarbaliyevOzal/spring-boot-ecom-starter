package org.example.usersservice.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(unique = true, nullable = false, length = 255)
    private String keycloakId;

    @Column(nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, insertable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    private Instant disabledAt;

    private Instant emailVerifiedAt;
}
