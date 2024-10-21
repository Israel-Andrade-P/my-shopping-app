package com.zel92.user.dto.response;

import com.zel92.user.entity.UserEntity;
import lombok.Builder;

@Builder
public record UserResponse(String fullName, String email, String role, String country, String city, String street, String zipCode) {

    public UserResponse(UserEntity userEntity) {
        this(userEntity.fullName(), userEntity.getEmail(), userEntity.getRole().getName(), null, null, null, null);
    }
}
