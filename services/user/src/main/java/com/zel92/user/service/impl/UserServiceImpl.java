package com.zel92.user.service.impl;

import com.zel92.user.dto.response.UserResponse;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.exception.UserNotFoundException;
import com.zel92.user.model.User;
import com.zel92.user.repository.UserRepository;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = getUserEntityByUserId(userId);
        userRepository.delete(userEntity);
    }

    private UserEntity getUserEntityByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("Invalid user with id: " + userId));
    }

    @Override
    public List<UserResponse> fetchAll() {
        return userRepository.findAll().stream().map(UserResponse::new).toList();
    }

    @Override
    public UserResponse fetchById(String userId) {
        return null;
    }
}
