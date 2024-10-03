package com.zel92.user.domain;

import com.zel92.user.model.User;
import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenData {
    private User user;
    private Claims claims;
    private Boolean valid;
    private List<GrantedAuthority> authorities;
}
