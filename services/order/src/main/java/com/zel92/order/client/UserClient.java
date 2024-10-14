package com.zel92.order.client;

import com.zel92.order.dto.UserResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/v1/auth/retrieve")
    public UserResponse retrieveUser(@RequestParam(name = "token") String token);
}
