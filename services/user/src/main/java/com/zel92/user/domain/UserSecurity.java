package com.zel92.user.domain;

import com.zel92.user.entity.CredentialEntity;
import com.zel92.user.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static org.springframework.security.core.authority.AuthorityUtils.*;

public class UserSecurity implements UserDetails {
    @Getter
    private final User user;
    private final CredentialEntity credential;
    @Getter
    private final String role;
    private final String username;
    private final List<? extends GrantedAuthority> authorities;

    public UserSecurity(User user, CredentialEntity credential){
        this.user = user;
        this.credential = credential;
        this.role = user.getRole();
        this.username = user.getEmail();
        authorities = null;
    }

    public UserSecurity(String username, List<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
        user = null;
        credential = null;
        role = "USER";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user != null ? commaSeparatedStringToAuthorityList(user.getAuthorities()) : authorities;
    }

    @Override
    public String getPassword() {
        return this.credential.getPassword();
    }

    @Override
    public String getUsername() {
        return user != null ? this.user.getEmail() : this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }
}
