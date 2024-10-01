package com.zel92.gateway.clients;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/v1/auth/validate")
    Boolean validateJwt(@RequestParam(name = "token") String jwt);
}
