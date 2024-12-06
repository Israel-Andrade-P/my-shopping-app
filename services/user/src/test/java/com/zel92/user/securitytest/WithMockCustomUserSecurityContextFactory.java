package com.zel92.user.securitytest;

import com.zel92.user.domain.CustomAuthentication;
import com.zel92.user.domain.UserSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Create a list of roles/authorities
        List<GrantedAuthority> authorities = Arrays.stream(annotation.roles())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Create a custom user
        UserSecurity customUserDetails = new UserSecurity(annotation.username(), authorities);

        // Create the Authentication object
        Authentication authentication = CustomAuthentication.authenticated(customUserDetails, authorities);

        // Set the Authentication in the SecurityContext
        context.setAuthentication(authentication);
        return context;
    }
}

