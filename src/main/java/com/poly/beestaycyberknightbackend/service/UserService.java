package com.poly.beestaycyberknightbackend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.Rank;
import com.poly.beestaycyberknightbackend.domain.Role;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.RegisterRequest;
import com.poly.beestaycyberknightbackend.dto.request.UserRequest;
import com.poly.beestaycyberknightbackend.dto.response.UserResponse;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.UserMapper;
import com.poly.beestaycyberknightbackend.repository.RankRepository;
import com.poly.beestaycyberknightbackend.repository.RoleRepository;
import com.poly.beestaycyberknightbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RankRepository rankRepository;
    private final PasswordEncoder passwordEncoder;

    
    public UserResponse handleCreateUser(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);

        Long roleId = userRequest.getRoleId();
        Integer rankId = userRequest.getRankId();

        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Rank rank = rankRepository.findById(rankId)
            .orElseThrow(() -> new AppException(ErrorCode.RANK_NOT_EXISTED));

        user.setRole(role);
        user.setRank(rank);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> fetchAllUser(){
        return userRepository.findAll()
            .stream()
            .map(userMapper::toUserResponse)
            .toList();
    }

    public UserResponse fetchUserById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("User với ID " + id + " không tồn tại"));

                
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
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

    public User updateRoleforUser(long userId, long roleId){
        Role role = roleRepository.findById(roleId).orElseThrow(()-> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setRole(role);
        return userRepository.save(user);
    }

    public User updateUser(Long id,UserRequest request){
        User user = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(()-> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Rank rank = rankRepository.findById(request.getRankId()).orElseThrow(()-> new AppException(ErrorCode.RANK_NOT_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setRank(rank);
        
        return userRepository.save(user);
    }

    public User registerRequesttoUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setFullname(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return user;
    }

    public boolean checkEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public Role getRoleByName(String roleName) {
    return this.roleRepository.findByRoleName(roleName)
        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
    }


    public UserResponse updateUserProfile(Long id, UserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    userMapper.updateProfileUser(user, request);
    user.setUpdateDate(LocalDateTime.now());

    userRepository.save(user);
    return userMapper.toUserResponse(user);
    }

}
