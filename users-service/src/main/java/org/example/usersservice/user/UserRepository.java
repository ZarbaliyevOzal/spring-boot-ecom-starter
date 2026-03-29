package org.example.usersservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    Page<User> findAllByDeletedAtIsNull(Pageable pageable);
}
