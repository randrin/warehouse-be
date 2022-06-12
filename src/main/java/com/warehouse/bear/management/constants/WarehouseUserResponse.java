package com.warehouse.bear.management.constants;

public class WarehouseUserResponse {
    public static final String WAREHOUSE_USER_CREATED = "User created successfully.";
    public static final String WAREHOUSE_USER_USERNAME_EXISTS = "User already exists with username ";
    public static final String WAREHOUSE_EXISTING_USER = "User already exists with user Id:  ";
    public static final String WAREHOUSE_USER_EMAIL_EXISTS = "User already exists with email ";
    public static final String WAREHOUSE_USER_LOGGED = "User logged successfully.";
    public static final String WAREHOUSE_USER_REGISTERED = "User registered successfully.";
    public static final String WAREHOUSE_USER_UPDATE = "User update successfully.";
    public static final String WAREHOUSE_USER_LOGOUT = "User logout successfully.";
    public static final String WAREHOUSE_USER_DELETED = "User deleted successfully.";
    public static final String WAREHOUSE_USER_UPDATED = "User updated successfully.";
    public static final String WAREHOUSE_USER_PASSWORD_GENERATED = "User Password generated successfully.";
    public static final String WAREHOUSE_USER_PASSWORD_CHANGED = "User Password changed successfully.";
    public static final String WAREHOUSE_USER_ACTIVATED = "User activated successfully.";
    public static final String WAREHOUSE_USER_DISABLED = "User disabled successfully.";
    public static final String WAREHOUSE_USER_UPDATE_PROFILE = "User Profile update successfully.";
    public static final String WAREHOUSE_USER_REFRESH_TOKEN = "Refresh token was expired. Please make a new signing request.";
    public static final String WAREHOUSE_USER_VERIFY_TOKEN = "Your token is valid and not expired. Good success in your job.";
    public static final String WAREHOUSE_USER_RESET_PASSWORD = "Confirmation reset link for your password is successfully send to ";
    public static final String WAREHOUSE_USER_VERIFICATION_EMAIL = "Confirmation confirmation  link for verify your email was send to ";
    public static final String WAREHOUSE_USER_CHANGE_PASSWORD = "User Password changed successfully.";
    public static final String WAREHOUSE_USER_CHANGE_STATUS = "User status changed successfully.";
    public static final String WAREHOUSE_USER_UPLOAD_PROFILE = "Upload profile file is done successfully.";

    // ERRORS
    public static final String WAREHOUSE_USER_UPDATE_PROFILE_NOT_FOUND = "User Profile can't be update.";
    public static final String WAREHOUSE_USER_DELETE_ERROR = "User hasn't deleted.";
    public static final String WAREHOUSE_USER_ERROR_LOGIN = "Bad Credentials Username/Email/UserID and (or) Password incorrect(s).";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID = "User not found with userId ";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME = "User not found with username/email/userID ";
    public static final String WAREHOUSE_USER_ERROR_VERIFICATION_EMAIL = "we have some problems for the verification email ";
    public static final String WAREHOUSE_ROLE_NOT_FOUND = "Role(s) not found in the system. Contact your administration.";
    public static final String WAREHOUSE_ERROR_ALREADY_USED = " is already used, please try another one";
    public static final String WAREHOUSE_USER_ERROR_PASSWORD = "The previous old user password isn't correct. Try again !!";
    public static final String WAREHOUSE_USER_ERROR_PASSWORD_NOT_MATCH = "User old password isn't correct. Try again !!";
    public static final String WAREHOUSE_USER_ERROR_DISABLE = "Your account isn't active. Contact your administration department for more information.";
    public static final String WAREHOUSE_USER_NOT_PRESENT = "We don't have any user registered yet.";
    public static final String WAREHOUSE_USER_ERROR_TOKEN = "Your token is expired or not valid. Please try login again.";
    public static final String WAREHOUSE_USER_ERROR_REFRESH_TOKEN = "The refresh token that you sending isn't not present in database. Try again";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_VERIFY_LINK = "Link and verify type that you are provided aren't corrects. Try resend the new link to complete the operation";
    public static final String WAREHOUSE_USER_ERROR_WRONG_ATTACHMENT = "Could not save this attachment ";
    public static final String WAREHOUSE_USER_ERROR_SAVE_ATTACHMENT = " File contains wrong characters. Try another one.";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_ATTACHMENT = "User file not found with userID: ";
}
