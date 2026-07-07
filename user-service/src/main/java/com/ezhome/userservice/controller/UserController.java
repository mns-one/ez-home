package com.ezhome.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhome.userservice.dto.EditUserDTO;
import com.ezhome.userservice.dto.UserAccountDTO;
import com.ezhome.userservice.service.UserService;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/user/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserAccountDTO findUserAccount(@RequestHeader("X-User-Id") String userId){
        UUID id = UUID.fromString(userId);
        return userService.getUserAccount(id);
    }

    @PutMapping("/edit")
    public UserAccountDTO editUserAccount(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody EditUserDTO payload){
        UUID id = UUID.fromString(userId);
        return userService.editUser(id, payload);
    }

}
