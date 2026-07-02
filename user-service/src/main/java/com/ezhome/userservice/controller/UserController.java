package com.ezhome.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhome.userservice.dto.CreateUserAccountDTO;
import com.ezhome.userservice.dto.UserAccountDTO;
import com.ezhome.userservice.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public UserAccountDTO createUserAccount(@Valid @RequestBody CreateUserAccountDTO payload) {
        return userService.createUserAccount(payload);
    }

    @GetMapping("/account/{id}")
    public UserAccountDTO findUserAccount(@PathVariable long id){
        return userService.getUserAccount(id);
    }

    @PutMapping("/account/{id}/edit")
    public UserAccountDTO editUserAccount(@PathVariable long id, @Valid @RequestBody CreateUserAccountDTO payload){
        return userService.editUserAccount(id, payload);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserAccount(@PathVariable long id){
        userService.deleteUserAccount(id);
    }

}
