package com.zel92.gateway.filters;

import com.zel92.gateway.clients.UserClient;
import com.zel92.gateway.exception.HeaderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
@Component
@Slf4j
public class TokenValidationFilter extends AbstractGatewayFilterFactory<TokenValidationFilter.Config> {

    private final RouteValidator validator;
    private final UserClient userClient;

    public TokenValidationFilter(RouteValidator validator, UserClient userClient){
        super(Config.class);
        this.validator = validator;
        this.userClient = userClient;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())){
                if (!exchange.getRequest().getHeaders().containsKey(AUTHORIZATION)){
                    throw new HeaderNotFoundException("No Authorization header in the request");
                }
                String authHeader = exchange.getRequest().getHeaders().get(AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                try {
                  var isTokenValid = userClient.validateJwt(authHeader);
                  if (!isTokenValid){
                      throw new RuntimeException("The token is invalid. Get a new one");
                  }
                }catch (Exception e){
                    log.error(e.getMessage());
                    throw new RuntimeException("Token error: " + e.getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }
    public static class Config{}
}
