package com.ezhome.userservice.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateUserDTO {

    @NotBlank(message = "Id is required")
    private UUID id;

    @NotBlank(message = "Email id is required")
    @Email(message = "Email must be valid")
    private String email;

}
