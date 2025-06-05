package com.poly.beestaycyberknightbackend.service;

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

    
}
