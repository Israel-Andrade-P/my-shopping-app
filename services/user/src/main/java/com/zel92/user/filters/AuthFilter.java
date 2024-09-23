package com.zel92.user.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.domain.CustomAuthentication;
import com.zel92.user.domain.Token;
import com.zel92.user.domain.UserSecurity;
import com.zel92.user.dto.request.LoginRequest;
import com.zel92.user.dto.response.LoginResponse;
import com.zel92.user.security.CustomAuthenticationManager;
import com.zel92.user.service.JwtService;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE;
import static com.zel92.user.enumeration.LoginType.LOGIN_ATTEMPT;
import static com.zel92.user.enumeration.LoginType.LOGIN_SUCCESS;
import static com.zel92.user.utils.UserUtils.*;

@Slf4j
public class AuthFilter extends AbstractAuthenticationProcessingFilter {
    private final UserService userService;
    private final JwtService jwtService;
    public AuthFilter(CustomAuthenticationManager manager, UserService userService, JwtService jwtService) {
        super(new AntPathRequestMatcher("/api/v1/users/login", HttpMethod.POST.name()), manager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try{
            var user = new ObjectMapper().configure(AUTO_CLOSE_SOURCE, true).readValue(request.getInputStream(), LoginRequest.class);
            userService.updateLoginAttempt(user.email(), LOGIN_ATTEMPT);
            var authentication = CustomAuthentication.unauthenticated(user.email(), user.password());
            return getAuthenticationManager().authenticate(authentication);
        }catch (Exception e){
            log.error(e.getMessage());
            RequestUtils.handleErrorResponse(request, response, e);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        var user = fromUserSecurity((UserSecurity) authentication.getPrincipal());
        userService.updateLoginAttempt(user.getEmail(), LOGIN_SUCCESS);
        String token = jwtService.createToken(user, Token::getAccess);
        var loginResponse = new LoginResponse(token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        var out = response.getOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, loginResponse);
        out.flush();
    }
}
