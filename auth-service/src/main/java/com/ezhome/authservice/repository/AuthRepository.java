package com.ezhome.authservice.repository;

import com.ezhome.authservice.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);
}
