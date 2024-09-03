package com.zel92.user.enumeration;

import lombok.Getter;

import static com.zel92.user.constants.Constants.*;
@Getter
public enum Authority {
    USER(USER_AUTHORITIES),
    MANAGER(MANAGER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    private final String value;

    Authority(String value) {
        this.value = value;
    }
}
