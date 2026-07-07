package com.ezhome.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class EditUserDTO {

    @NotNull
    private String username;

    @NotNull
    private String city;

    @NotNull
    private String pofileImageUrl;
    
}
