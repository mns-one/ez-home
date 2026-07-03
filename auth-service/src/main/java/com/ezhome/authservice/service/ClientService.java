package com.ezhome.authservice.service;

import com.ezhome.authservice.dto.LoginRequestDTO;
import com.ezhome.authservice.exception.CustomException;
import com.ezhome.authservice.model.Client;
import com.ezhome.authservice.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ClientService {

  private final ClientRepository clientRepository;
  private final PasswordEncoder passwordEncoder;

  public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
    this.clientRepository = clientRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // register a new client
  public void registerClient(LoginRequestDTO loginRequestDTO) {
    // Check if the email already exists
    if (clientRepository.findByEmail(loginRequestDTO.getEmail()).isPresent()) {
      throw new CustomException("Email linked to existing account");
    }

    // Create a new client and save it to the database
    Client newClient = new Client();
    newClient.setEmail(loginRequestDTO.getEmail());
    newClient.setPassword(passwordEncoder.encode(loginRequestDTO.getPassword()));
    newClient.setRole("USER"); // Set default role

    clientRepository.save(newClient);

  }

  // find client by email
  public Optional<Client> findByEmail(String email) {
    return clientRepository.findByEmail(email);
  }

}