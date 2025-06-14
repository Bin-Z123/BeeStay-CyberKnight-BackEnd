package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.repository.RoleRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    
    public User handleCreateUser(User user){
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id){
        return this.userRepository.findById(id)
                .orElseThrow();
    }

    public List<User> fetchAllUser(){
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User reqUser){
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setFullname(reqUser.getFullname());
            currentUser.setEmail(reqUser.getEmail());
            currentUser.setPassword(reqUser.getPassword());
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public User updateRoleforUser(Long userId, Long roleId){
        Role role = roleRepository.findById(roleId).orElseThrow(()-> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setRole(role);

        return userRepository.save(user);
    }
}
