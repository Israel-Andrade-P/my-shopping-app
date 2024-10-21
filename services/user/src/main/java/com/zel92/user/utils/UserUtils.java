package com.zel92.user.utils;

import com.zel92.user.domain.UserSecurity;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.dto.response.UserResponse;
import com.zel92.user.entity.LocationEntity;
import com.zel92.user.entity.RoleEntity;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.event.AccVerificationEvent;
import com.zel92.user.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.function.Supplier;

public class UserUtils {

    public static UserEntity buildUserEntity(UserRequest user, RoleEntity role){
        return UserEntity.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .userId(suppliesUserId.get())
                .email(user.email())
                .telephone(user.telephone())
                .dob(user.dob())
                .lastLogin(LocalDateTime.now())
                .loginAttempts(0)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .enabled(false)
                .role(role)
                .build();
    }

    public static AccVerificationEvent buildAccVerificationEvent(String userFullName, String email, String key){
        return new AccVerificationEvent(userFullName, email, key);
    }

    public static User fromUserEntity(UserEntity userEntity){
        return User.builder()
                .id(userEntity.getId())
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .dob(userEntity.getDob())
                .lastLogin(userEntity.getLastLogin())
                .loginAttempts(userEntity.getLoginAttempts())
                .accountNonLocked(userEntity.isAccountNonLocked())
                .accountNonExpired(userEntity.isAccountNonExpired())
                .enabled(userEntity.isEnabled())
                .role(userEntity.getRole().getName())
                .authorities(userEntity.getRole().getAuthority().getValue())
                .build();
    }

    public static User fromUserSecurity(UserSecurity userSecurity){
        var stringBuilder = new StringBuilder();
        var user = User.builder()
                .email(userSecurity.getUsername())
                .role(userSecurity.getUser().getRole())
                .build();
        userSecurity.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().forEach(string -> {
            var result = stringBuilder.append(string);
            user.setAuthorities(result.toString());
        });
        return user;
    }

    public static UserResponse toUserResponse(User user, LocationEntity location) {
        return new UserResponse(user.fullName(), user.getEmail(), user.getRole(), location.getCountry(), location.getCity(), location.getStreet(), location.getZipCode());
    }

    private static final Supplier<String> suppliesUserId = () -> {
        String pool = "0123456789ABCDEFGHIJ";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++){
            int randomIndex = random.nextInt(pool.length());
            builder.append(pool.charAt(randomIndex));
        }
        return builder.toString();
    };
}
