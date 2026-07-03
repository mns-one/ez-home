package com.ezhome.authservice.service;

import com.ezhome.authservice.dto.LoginRequestDTO;
import com.ezhome.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

  private final ClientService clientService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(ClientService clientService, PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil) {
    this.clientService = clientService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  // authenticate user and generate JWT token
  public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {

    Optional<String> token = clientService.findByEmail(loginRequestDTO.getEmail())
        .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
            u.getPassword()))
        .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));

    return token;

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

