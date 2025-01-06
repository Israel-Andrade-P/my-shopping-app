package com.zel92.user.filters;

import com.zel92.user.domain.CustomAuthentication;
import com.zel92.user.domain.TokenData;
import com.zel92.user.domain.UserSecurity;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.exception.InvalidJwtException;
import com.zel92.user.repository.UserRepository;
import com.zel92.user.service.AuthService;
import com.zel92.user.service.JwtService;
import com.zel92.user.utils.UserUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/api/v1/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        var jwt = authHeader.substring(7);

        if (jwtService.validateToken(jwt)){
            var user = jwtService.getTokenData(jwt, TokenData::getUser);
            var userSec = new UserSecurity(user, null);
            var auth = CustomAuthentication.authenticated(userSec, userSec.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);

    }
}
