package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public User handleCreateUser(User user){
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id){
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public List<User> fetchAllUser(){
        return this.userRepository.findAll();
    }

    // public User handleUpdateUser(User reqUser){
    //     User currentUser = this.fetchUserById(reqUser.getId());
    //     if (currentUser != null) {
    //         currentUser.setFullname(reqUser.getFullname());
    //         currentUser.setEmail(reqUser.getEmail());
    //         currentUser.setPassword(reqUser.getPassword());
    //         currentUser = this.userRepository.save(currentUser);
    //     }
    //     return currentUser;
    // }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}
