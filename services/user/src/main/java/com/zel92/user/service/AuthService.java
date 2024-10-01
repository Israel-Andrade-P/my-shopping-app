package com.zel92.user.service;

import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.entity.CredentialEntity;
import com.zel92.user.enumeration.LoginType;
import com.zel92.user.model.User;

public interface AuthService {

    void createUser(UserRequest user);
    void verifyAccount(String key);
    User getUserByEmail(String email);
    void updateLoginAttempt(String email, LoginType loginType);
    CredentialEntity getCredentialByUserId(Long userId);
}
