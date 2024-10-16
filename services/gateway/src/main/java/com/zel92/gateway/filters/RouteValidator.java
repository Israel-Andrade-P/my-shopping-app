package com.zel92.gateway.filters;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApis = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/verify",
            "/api/v1/auth/login",
            "/eureka");
    public Predicate<ServerHttpRequest> isSecured = request -> openApis.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
