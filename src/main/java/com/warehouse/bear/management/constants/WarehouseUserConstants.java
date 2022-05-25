package com.warehouse.bear.management.constants;

public class WarehouseUserConstants {

    // TOOLS
    public static final String WAREHOUSE_SECRET_KEY = "warehouse-be-management";
    public static final int WAREHOUSE_EXPIRATION_TOKEN = 1000 * 60 * 60 * 8;
    public static final int WAREHOUSE_EXPIRATION_LINK = 15;
    public static final String WAREHOUSE_RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\\\|;:\\'\\\",<.>/?\"";
    public static final String WAREHOUSE_RANDOM_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String WAREHOUSE_RANDOM_NUMBERS = "0123456789";
    public static final String WAREHOUSE_AUTHORIZATION = "Authorization";
    public static final String WAREHOUSE_HEADER = "Bearer ";
    public static final String WAREHOUSE_ROLE_ADMIN = "ROLE_ADMIN";
    public static final String WAREHOUSE_ROLE_USER = "ROLE_USER";
    public static final String WAREHOUSE_ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String[] WAREHOUSE_ADMIN_ACCESS = {"ROLE_ADMIN", "ROLE_MODERATOR"};
    public static final String[] WAREHOUSE_MODERATOR_ACCESS = {"ROLE_MODERATOR"};
    public static final String WAREHOUSE_SUBJECT_EMAIL_FORGOT_PASSWORD = "Confirmation send reset your password";
    public static final String WAREHOUSE_VERIFY_TYPE_RESET_PASSWORD = "RESET_PASSWORD";
    public static final String WAREHOUSE_VERIFY_TYPE_EMAIL = "EMAIL_VERIFICATION";
    public static final String WAREHOUSE_VERIFY_TYPE_PASSWORD_REMINDER = "PASSWORD_REMINDER";

    // MODELS
    public static final String WAREHOUSE_FULLNAME_REQUIRED = "Fullname is required";
    public static final String WAREHOUSE_GENDER_REQUIRED = "Gender is required";
    public static final String WAREHOUSE_USERNAME_REQUIRED = "Username is required";
    public static final String WAREHOUSE_EMAIL_REQUIRED = "Email is required";
    public static final String WAREHOUSE_PASSWORD_REQUIRED = "Password is required";

    public static final String WAREHOUSE_USER_NAME = "User name is required";
    public static final String WAREHOUSE_USER_EMAIL = "User email is required";
    public static final String WAREHOUSE_USER_EMAIL_VALID = "Please enter the valid email";
    public static final String WAREHOUSE_USER_PHONE = "User phone is required";
    public static final String WAREHOUSE_USER_PHONE_VALID = "Please enter the valid phone";
    public static final String WAREHOUSE_USER_PASSWORD = "User password is required";

    // PATTERNS
    public static final String WAREHOUSE_PATTERN_EMAIL = "^([a-zA-Z0-9_-]{4,20})@([a-zA-Z]{3,6})\\.([a-zA-Z]{2,5})$";
    public static final String WAREHOUSE_PATTERN_PHONE = "^([+]\\d{2})?\\d{10}$";
    public static final String WAREHOUSE_PATTERN_PASSWORD = "^(?=.*\\d).{4,12}$";
    public static final String WAREHOUSE_PATTERN_DATE = "uuuu/MM/dd HH:mm:ss";

}
