package com.poly.beestaycyberknightbackend.controller;


import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @PostMapping("/users")
    // public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
    //     String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
    //     postManUser.setPassword(hashPassword);
    //     User user = this.userService.handleCreateUser(postManUser);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(user);
    // }

    @PostMapping("/users")
    public String createNewUser(@RequestBody User postManUser) {
        this.userService.handleCreateUser(postManUser);
        return "User created successfully";
    }
    
}
