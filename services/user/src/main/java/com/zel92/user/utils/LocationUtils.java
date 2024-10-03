package com.zel92.user.utils;

import com.zel92.user.dto.request.LocationRequest;
import com.zel92.user.entity.LocationEntity;
import com.zel92.user.entity.UserEntity;

public class LocationUtils {

    public static LocationEntity buildLocation(LocationRequest location, UserEntity user){
        return LocationEntity.builder()
                .country(location.country())
                .city(location.city())
                .street(location.street())
                .zipCode(location.zipCode())
                .user(user)
                .build();
    }
}
