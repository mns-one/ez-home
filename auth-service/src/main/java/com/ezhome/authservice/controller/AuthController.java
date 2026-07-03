package com.ezhome.authservice.controller;

import com.ezhome.authservice.dto.LoginRequestDTO;
import com.ezhome.authservice.dto.LoginResponseDTO;
import com.ezhome.authservice.exception.AccessException;
import com.ezhome.authservice.service.AuthService;
import com.ezhome.authservice.service.ClientService;

import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/auth/v1")
public class AuthController {

  private final AuthService authService;
  private final ClientService clientService;

  public AuthController(AuthService authService, ClientService clientService) {
    this.authService = authService;
    this.clientService = clientService;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
    clientService.registerClient(loginRequestDTO);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

    Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

    if (tokenOptional.isEmpty()) {
      throw new AccessException("Invalid email or password");
    }

    String token = tokenOptional.get();
    return ResponseEntity.ok(new LoginResponseDTO(token));

  }

  @GetMapping("/validate")
  public ResponseEntity<Void> validateToken(@Valid @RequestHeader("Authorization") String authHeader) {

    // Authorization: Bearer <token>
    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new AccessException("Missing or invalid token");
    }

    boolean res = authService.validateToken(authHeader.substring(7));

    if(!res) {
      throw new AccessException("Invalid or expired token");
    }

    return ResponseEntity.ok().build();

  }

}

