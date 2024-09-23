package com.zel92.user.security;

import com.zel92.user.domain.CustomAuthentication;
import com.zel92.user.domain.UserSecurity;
import com.zel92.user.exception.GenericException;
import com.zel92.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var customAuth = (CustomAuthentication) authentication;
        var user = (UserSecurity) userDetailsService.loadUserByUsername(customAuth.getEmail());
        if (user != null){
            validAccount.accept(user);

            if (encoder.matches(customAuth.getPassword(), user.getPassword())){
                return CustomAuthentication.authenticated(user, user.getAuthorities());
            }else throw new BadCredentialsException("Email and/or password is incorrect. Please try again");
        }else throw new GenericException("User not found");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.isAssignableFrom(authentication);
    }

    private final Consumer<UserSecurity> validAccount = userSecurity -> {
        if (!userSecurity.isAccountNonLocked()) {throw new LockedException("Your account is currently locked");}
        if (!userSecurity.isEnabled()) {throw new DisabledException("Your account is currently disabled");}
        if (!userSecurity.isAccountNonExpired()) {throw new DisabledException("Your account has expired. Please contact administrator");}
    };
}
