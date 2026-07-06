package com.ezhome.userservice.service;

import org.springframework.stereotype.Service;

import com.ezhome.userservice.dto.CreateUserAccountDTO;
import com.ezhome.userservice.dto.UserAccountDTO;
import com.ezhome.userservice.entity.User;
import com.ezhome.userservice.exception.CustomException;
import com.ezhome.userservice.repository.UserRepository;


@Service

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // create a new user account
    public UserAccountDTO createUserAccount(CreateUserAccountDTO payload) {

        if(userRepository.existsByEmailAndUserIdNot(payload.getEmail(), null)){
            throw new CustomException("Email linked to another user account");
        }

        User newUser = User.builder()
                .username(payload.getUsername())
                .email(payload.getEmail())
                .build();

        User savedUser = userRepository.save(newUser);

        return mapToUserAccountDTO(savedUser);

    }

    // find user account by userId
    public UserAccountDTO getUserAccount(long userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(
                    "User account not found for given userId"
                ));
        return mapToUserAccountDTO(user);

    }

    // edit user account by userId
    public UserAccountDTO editUserAccount(long userId, CreateUserAccountDTO payload) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(
                    "User account not found for given userId"
                ));

        if(userRepository.existsByEmailAndUserIdNot(payload.getEmail(), userId)){
            throw new CustomException("Email linked to another user account");
        }

        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());

        User updatedUser = userRepository.save(user);

        return mapToUserAccountDTO(updatedUser);

    }

    // delete user account by userId
    public void deleteUserAccount(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                    "User account not found for given email"
                ));
        userRepository.delete(user);

    }

    // map User entity to UserAccountDTO
    private UserAccountDTO mapToUserAccountDTO(User user) {
        return UserAccountDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    
}
