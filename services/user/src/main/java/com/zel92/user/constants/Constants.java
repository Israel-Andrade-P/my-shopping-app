package com.zel92.user.constants;

public class Constants {
    public static final String USER_AUTHORITIES = "product:create,product:read,product:update,product:delete";
    public static final String ADMIN_AUTHORITIES = "user:create,user:read,user:update,product:create,product:read,product:update,product:delete";
    public static final String SUPER_ADMIN_AUTHORITIES = "user:create,user:read,user:update,user:delete,product:create,product:read,product:update,product:delete";
    public static final String MANAGER_AUTHORITIES = "product:create,product:read,product:update,product:delete";
    public static final Integer EXPIRATION = 30;
    public static final String AUTHORITIES = "authorities";
    public static final String ROLE = "role";
    public static final String ZEL_CODING_INC = "Zel-Coding.Inc";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String EMAIL_PROTECTED = "EMAIL_PROTECTED";
    public static final String PASSWORD_PROTECTED = "PASSWORD_PROTECTED";
    public static final String LOGIN_PATH = "/api/v1/auth/login";
    public static final String[] WHITE_LIST = {"/api/v1/auth/register", "/api/v1/auth/verify", "/api/v1/auth/login", "/api/v1/auth/validate"};
    public static final String ACCOUNT_CREATED_MESSAGE = "Your account has been created successfully. Please check your email to enable your account.";
    public static final String ACCOUNT_VERIFIED_MESSAGE = "Your account has been verified";
}
