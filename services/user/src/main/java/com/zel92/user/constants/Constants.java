package com.zel92.user.constants;

public class Constants {
    public static final String USER_AUTHORITIES = "product:create,product:read,product:update,product:delete";
    public static final String ADMIN_AUTHORITIES = "user:create,user:read,user:update,product:create,product:read,product:update,product:delete";
    public static final String SUPER_ADMIN_AUTHORITIES = "user:create,user:read,user:update,user:delete,product:create,product:read,product:update,product:delete";
    public static final String MANAGER_AUTHORITIES = "product:create,product:read,product:update,product:delete";
    public static final Integer EXPIRATION = 30;
}
