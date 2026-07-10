package com.ezhome.userservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ezhome.userservice.dto.CreateUserDTO;
import com.ezhome.userservice.dto.EditUserDTO;
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
    public User createUser(CreateUserDTO payload) {

        if(userRepository.existsByEmailAndIdNot(payload.getEmail(), null)){
            throw new CustomException("Email linked to another user account");
        }

        User newUser = User.builder()
                .id((payload.getId()))
                .username("user")
                .email(payload.getEmail())
                .build();

        User createdUser = userRepository.save(newUser);
        return createdUser;

    }

    // find user account by userId
    public UserAccountDTO getUserAccount(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                    "User account not found for given email"
                ));
        return mapToUserAccountDTO(user);

    }

    // edit user account in db
    public UserAccountDTO editUser(UUID id, EditUserDTO payload) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                    "User account not found for given email"
                ));

        user.setUsername(payload.getUsername());
        user.setCity(payload.getCity());
        user.setPofileImageUrl(payload.getPofileImageUrl());

        User updatedUser = userRepository.save(user);

        return mapToUserAccountDTO(updatedUser);

    }

    // delete user account from db
    public void deleteUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                    "User account not found for given email"
                ));
        userRepository.delete(user);

    }

    // map User entity to UserAccountDTO
    private UserAccountDTO mapToUserAccountDTO(User user) {
        return UserAccountDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .city(user.getCity())
                .pofileImageUrl(user.getPofileImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }

    
}
