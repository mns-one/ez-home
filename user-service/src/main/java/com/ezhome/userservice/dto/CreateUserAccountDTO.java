package com.ezhome.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateUserAccountDTO {

    @NotBlank(message = "Name is required")
    private String username;

    @NotBlank(message = "Email id is required")
    @Email(message = "Email must be valid")
    private String email;

}
