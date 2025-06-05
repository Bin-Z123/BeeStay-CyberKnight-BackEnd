package com.poly.beestaycyberknightbackend.controller;


import org.springframework.web.bind.annotation.RestController;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.service.UserService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    public User createNewUser(@RequestBody User postManUser) {
        User newUser = this.userService.handleCreateUser(postManUser);
        return newUser;
    }
    
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "Delete successsuccess";

    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return this.userService.fetchUserById(id);
        // User fetchUser = this.userService.fetchUserById(id);
        // return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    @GetMapping("/users")
    public List<User> getAllUser(){
        return this.userService.fetchAllUser();
    }
    
}
