package com.zel92.user.service;

import com.zel92.user.domain.Token;
import com.zel92.user.domain.TokenData;
import com.zel92.user.dto.response.UserResponse;
import com.zel92.user.model.User;

import java.util.function.Function;

public interface JwtService {

    String createToken(User user, Function<Token, String> tokenFunction);
    <T> T getTokenData(String token, Function<TokenData, T> tokenDataFunction);
    Boolean validateToken(String jwt);
    UserResponse retrieveUser(String token);
}
