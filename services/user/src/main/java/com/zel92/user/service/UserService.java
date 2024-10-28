package com.zel92.user.service;

import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.dto.response.UserInfoResp;
import com.zel92.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    void deleteUser(String userId);
    List<UserInfoResp> fetchAll();

    UserInfoResp fetchById(String userId);

    void updateRole(String userId, String role);

    void updateUser(String userId, UserRequest user);
}
