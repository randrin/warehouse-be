package com.warehouse.bear.management.constants;

public class WarehouseUserConstants {

    // TOOLS
    public static final String WAREHOUSE_SECRET_KEY = "warehouse-be-management";
    public static final int WAREHOUSE_EXPIRATION_TOKEN = 1000 * 60 * 60 * 10;
    public static final String WAREHOUSE_RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\\\|;:\\'\\\",<.>/?\"";
    public static final String WAREHOUSE_RANDOM_NUMBERS = "0123456789";
    public static final String WAREHOUSE_AUTHORIZATION = "Authorization";
    public static final String WAREHOUSE_HEADER = "Bearer ";
    public static final String WAREHOUSE_ROLE_ADMIN = "ADMIN";
    public static final String WAREHOUSE_ROLE_USER = "USER";
    public static final String WAREHOUSE_ROLE_MODERATOR = "MODERATOR";
    public static final String[] WAREHOUSE_ADMIN_ACCESS = {"ADMIN", "MODERATOR"};
    public static final String[] WAREHOUSE_MODERATOR_ACCESS = {"MODERATOR"};

    // MODEL
    public static final String WAREHOUSE_USERNAME_REQUIRED = "Username is required";
    public static final String WAREHOUSE_EMAIL_REQUIRED = "Email is required";
    public static final String WAREHOUSE_PASSWORD_REQUIRED = "Password is required";

    public static final String WAREHOUSE_USER_NAME = "User name is required";
    public static final String WAREHOUSE_USER_EMAIL = "User email is required";
    public static final String WAREHOUSE_USER_EMAIL_VALID = "Please enter the valid email";
    public static final String WAREHOUSE_USER_PHONE = "User phone is required";
    public static final String WAREHOUSE_USER_PHONE_VALID = "Please enter the valid phone";
    public static final String WAREHOUSE_USER_PASSWORD = "User password is required";

    public static final String WAREHOUSE_PATTERN_EMAIL = "^([a-zA-Z0-9_-]{4,20})@([a-zA-Z]{3,6})\\.([a-zA-Z]{2,5})$";
    public static final String WAREHOUSE_PATTERN_PHONE = "^([+]\\d{2})?\\d{10}$";
    public static final String WAREHOUSE_PATTERN_PASSWORD = "^(?=.*\\d).{4,12}$";

}
