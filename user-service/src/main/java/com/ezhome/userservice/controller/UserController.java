package com.ezhome.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhome.userservice.dto.CreateUserAccountDTO;
import com.ezhome.userservice.dto.UserAccountDTO;
import com.ezhome.userservice.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account/{id}")
    public UserAccountDTO findUserAccount(@PathVariable long id){
        return userService.getUserAccount(id);
    }

    @PutMapping("/account/{id}/edit")
    public UserAccountDTO editUserAccount(@PathVariable long id, @Valid @RequestBody CreateUserAccountDTO payload){
        return userService.editUserAccount(id, payload);
    }

}
