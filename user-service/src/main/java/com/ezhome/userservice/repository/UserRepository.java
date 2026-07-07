package com.ezhome.userservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezhome.userservice.entity.User;


public interface UserRepository extends JpaRepository<User,UUID> {
    
    Boolean existsByEmailAndIdNot(String email, UUID id);

    Optional<User> findByEmail(String email);


}
