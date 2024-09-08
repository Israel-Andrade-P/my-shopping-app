package com.zel92.user.utils;

import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.entity.RoleEntity;
import com.zel92.user.entity.UserEntity;

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

    private static final Supplier<String> suppliesUserId = () -> {
        String pool = "0123456789ABCDEFGHIJ";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        int c = 0;
        for (int i = 0; i < 9; i++){
            int randomIndex = random.nextInt(pool.length());
            builder.append(pool.charAt(randomIndex));
        }
        return builder.toString();
    };
}
