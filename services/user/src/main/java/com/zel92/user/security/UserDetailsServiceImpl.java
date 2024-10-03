package com.zel92.user.security;

import com.zel92.user.domain.UserSecurity;
import com.zel92.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userService.getUserByEmail(email);
        var credential = userService.getCredentialByUserId(user.getId());
        return new UserSecurity(user, credential);
    }
}
