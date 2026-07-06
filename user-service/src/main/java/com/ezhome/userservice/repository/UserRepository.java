package com.ezhome.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezhome.userservice.entity.User;


public interface UserRepository extends JpaRepository<User,Long> {
    
    Boolean existsByEmailAndUserIdNot(String email, Long userId);

    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(long userId);

}
