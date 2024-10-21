package com.zel92.user.service;

import com.zel92.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    void deleteUser(String userId);
    List<UserResponse> fetchAll();

    UserResponse fetchById(String userId);
}
