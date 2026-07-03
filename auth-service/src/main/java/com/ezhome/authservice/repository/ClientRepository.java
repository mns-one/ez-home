package com.ezhome.authservice.repository;

import com.ezhome.authservice.model.Client;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, UUID> {
  Optional<Client> findByEmail(String email);
}
