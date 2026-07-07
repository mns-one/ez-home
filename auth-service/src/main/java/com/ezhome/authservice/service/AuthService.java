package com.ezhome.authservice.service;

import com.ezhome.authservice.dto.LoginRequestDTO;
import com.ezhome.authservice.exception.CustomException;
import com.ezhome.authservice.grpc.UserServiceGrpcClient;
import com.ezhome.authservice.entity.Role;
import com.ezhome.authservice.entity.User;
import com.ezhome.authservice.repository.AuthRepository;
import com.ezhome.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AuthService {

  private final AuthRepository authRepository;
  private final UserServiceGrpcClient userServiceGrpcClient;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(AuthRepository authRepository, UserServiceGrpcClient userServiceGrpcClient, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.authRepository = authRepository;
    this.userServiceGrpcClient = userServiceGrpcClient;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  // register a new user
  public void registerUser(LoginRequestDTO loginRequestDTO) {

    // return if email already exists in db
    if (authRepository.findByEmail(loginRequestDTO.getEmail()).isPresent()) {
      throw new CustomException("Email linked to existing account");
    }

    User newUser = new User();
    newUser.setEmail(loginRequestDTO.getEmail());
    newUser.setPasswordhash(passwordEncoder.encode(loginRequestDTO.getPassword()));
    newUser.setRole(Role.USER); // Set default role

    try{
      // poll the User-service before creating a user account
      userServiceGrpcClient.pollUserService("ping");
      User userAccount = authRepository.save(newUser);
      userServiceGrpcClient.createUserAccount(userAccount.getId().toString(), userAccount.getEmail());
    }
    catch (Exception e) {
      // log error and delete auth account if it was created before gRPC call
      log.error("Error while registering client: {}", e.getMessage());

      Optional<User> userAccount = authRepository.findByEmail(loginRequestDTO.getEmail())
      .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
          u.getPasswordhash()));

      if (userAccount.isPresent()) {
        authRepository.delete(userAccount.get());
      }  

      throw new CustomException("Failed to register client");
    }

  }

  // authenticate user and generate JWT token
  public Optional<String> authenticateUser(LoginRequestDTO loginRequestDTO) {

    Optional<String> token = authRepository.findByEmail(loginRequestDTO.getEmail())
        .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
            u.getPasswordhash()))
        .map(u -> jwtUtil.generateToken(u.getId().toString(), u.getRole().name()));

    return token;

  }

  // delete user aacount
  public void deleteUser(LoginRequestDTO loginRequestDTO) {

    Optional<User> userAccount = authRepository.findByEmail(loginRequestDTO.getEmail())
      .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
          u.getPasswordhash()));

    // return if no account found
    if (!userAccount.isPresent()) {
      throw new CustomException("Incorrect email or password");
    }      

    try{
      // poll the User-service before deleting a user account
      userServiceGrpcClient.pollUserService("ping");
      userServiceGrpcClient.deleteUserAccount(userAccount.get().getId().toString());
      authRepository.delete(userAccount.get());
    }
    catch (Exception e) {
      log.error("Error while deleting user: {}", e.getMessage());
      throw new CustomException("Failed to delete user account");
    }

  }

  // validate JWT token
  public boolean validateToken(String token) {

    try {
      jwtUtil.validateToken(token);
      return true;
    }
    catch (JwtException e){
      return false;
    }

  }

}

