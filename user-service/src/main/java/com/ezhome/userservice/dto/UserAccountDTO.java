package com.ezhome.userservice.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserAccountDTO {

    private long userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;

}
