package com.ezhome.userservice.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserAccountDTO {

    private String username;
    private String email;
    private String city;
    private String pofileImageUrl;
    private LocalDateTime createdAt;

}
