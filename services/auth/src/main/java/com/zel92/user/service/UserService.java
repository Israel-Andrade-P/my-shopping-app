package com.zel92.user.service;

import com.zel92.user.dto.request.UserRequest;

public interface UserService {

    void createUser(UserRequest user);
    void verifyAccount(String key);
}
