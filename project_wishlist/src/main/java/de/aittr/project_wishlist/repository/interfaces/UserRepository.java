package de.aittr.project_wishlist.repository.interfaces;

import de.aittr.project_wishlist.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByActiveFalseAndCreatedAtBefore(LocalDateTime cutoffTime);
}

