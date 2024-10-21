package com.zel92.user.domain;

import com.zel92.user.exception.AuthStatusException;
import com.zel92.user.model.User;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

import static com.zel92.user.constants.Constants.EMAIL_PROTECTED;
import static com.zel92.user.constants.Constants.PASSWORD_PROTECTED;

public class CustomAuthentication extends AbstractAuthenticationToken {
    private UserSecurity user;
    @Getter
    private String email;
    @Getter
    private String password;
    private boolean authenticated;

    private CustomAuthentication(String email, String password){
        super(AuthorityUtils.NO_AUTHORITIES);
        this.email = email;
        this.password = password;
        this.authenticated = false;
    }

    private CustomAuthentication(UserSecurity user, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.user = user;
        this.email = EMAIL_PROTECTED;
        this.password = PASSWORD_PROTECTED;
        this.authenticated = true;
    }

    public static CustomAuthentication unauthenticated(String email, String password){
        return new CustomAuthentication(email, password);
    }

    public static CustomAuthentication authenticated(UserSecurity user, Collection<? extends GrantedAuthority> authorities){
        return new CustomAuthentication(user, authorities);
    }

    @Override
    public Object getCredentials() {
        return PASSWORD_PROTECTED;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new AuthStatusException("Can't touch this");
    }
}
