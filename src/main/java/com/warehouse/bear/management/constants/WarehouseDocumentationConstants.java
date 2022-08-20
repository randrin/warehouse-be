package com.warehouse.bear.management.constants;

import io.swagger.annotations.ApiResponse;

public class WarehouseDocumentationConstants {

    public static final String WAREHOUSE_AUTH_API_NAME = "Warehouse Authentification/Authorization API";
    public static final String WAREHOUSE_API_RESPONSE_400 = "Username/Password are not corrects.";
    public static final String WAREHOUSE_API_RESPONSE_401 = "User isn't authorized to perform this operation.";
    public static final String WAREHOUSE_API_RESPONSE_403 = "Forbidden user to perform this operation.";
    public static final String WAREHOUSE_API_RESPONSE_500 = "Internal error. Try contact your administration.";
<<<<<<< Updated upstream
    public static final String WAREHOUSE_OPERATION_LOGIN = "Login to warehouse system";
    public static final String WAREHOUSE_OPERATION_REGISTER = "Register to warehouse system";
    public static final String WAREHOUSE_OPERATION_LOGOUT = "Logout to warehouse system";
    public static final String WAREHOUSE_OPERATION_REFRESH_TOKEN = "Refresh your token to warehouse system";
    public static final String WAREHOUSE_OPERATION_GET_ALL_USERS = "Get all users registered in warehouse system";
    public static final String WAREHOUSE_OPERATION_UPLOAD = "file upload in warehouse system";
    public static final String WAREHOUSE_OPERATION_DOWNLOAD = "file download in warehouse system";
    public static final String WAREHOUSE_OPERATION_VERIFY_TOKEN = "Verify if user's token is valid to warehouse system";
    public static final String WAREHOUSE_OPERATION_FORGOT_PASSWORD = "Send email link to user to reset our password in warehouse system";
    public static final String WAREHOUSE_VERIFY_EMAIL = "Send email link to user to verify his email";
    public static final String WAREHOUSE_OPERATION_VERIFY_LINK = "Verify the user link provided with the verify type";
    public static final String WAREHOUSE_OPERATION_RESET_PASSWORD = "Reset your password to warehouse system";
    public static final String WAREHOUSE_OPERATION_CHANGE_PASSWORD = "Change your password to warehouse system";
    public static final String WAREHOUSE_OPERATION_CHANGE_STATUS = "Activate or disable user in warehouse system";
=======

    // API ADMIN OPERATIONS
    public static final String WAREHOUSE_ADMIN_OPERATION_INSERT_USER = "Admin insert new user in warehouse system.";
    public static final String WAREHOUSE_ADMIN_OPERATION_ACTIVE_OR_DISABLE_USER = "Admin activated or disabled new user in warehouse system.";

    // API OPERATIONS
    public static final String WAREHOUSE_OPERATION_LOGIN = "Login to warehouse system.";
    public static final String WAREHOUSE_OPERATION_REGISTER = "Register to warehouse system.";
    public static final String WAREHOUSE_OPERATION_LOGOUT = "Logout to warehouse system.";
    public static final String WAREHOUSE_OPERATION_REFRESH_TOKEN = "Refresh your token to warehouse system.";
    public static final String WAREHOUSE_OPERATION_GET_ALL_USERS = "Get all users registered in warehouse system.";
    public static final String WAREHOUSE_OPERATION_UPLOAD = "File upload in warehouse system.";
    public static final String WAREHOUSE_OPERATION_DOWNLOAD = "File download in warehouse system.";
    public static final String WAREHOUSE_OPERATION_DELETE_FILE = "File delete in warehouse system.";
    public static final String WAREHOUSE_OPERATION_VERIFY_TOKEN = "Verify if user's token is valid to warehouse system.";
    public static final String WAREHOUSE_OPERATION_FORGOT_PASSWORD = "Send email link to user to reset our password in warehouse system.";
    public static final String WAREHOUSE_OPERATION_VERIFY_EMAIL = "Send email link to user to verify his email.";
    public static final String WAREHOUSE_OPERATION_VERIFY_LINK = "Verify the user link provided with the verify type.";
    public static final String WAREHOUSE_OPERATION_RESET_PASSWORD = "Reset your password to warehouse system.";
    public static final String WAREHOUSE_OPERATION_CHANGE_PASSWORD = "Change your password to warehouse system.";
    public static final String WAREHOUSE_OPERATION_CHANGE_STATUS = "Activate or disable user in warehouse system.";
    public static final String WAREHOUSE_OPERATION_DELETE_USER = "Delete user registered in warehouse system.";
    public static final String WAREHOUSE_OPERATION_FIND_USER = "Find user by userID in warehouse system.";
    public static final String WAREHOUSE_OPERATION_FIND_USER_BY_LINK_TYPE = "Find user by userID, verify link and verify type in warehouse system.";
    public static final String WAREHOUSE_OPERATION_UPDATE_USER = "Update user in warehouse system.";
    public static final String WAREHOUSE_OPERATION_VIEW_PDF = "view all data user on pdf.";
    public static final String WAREHOUSE_OPERATION_EXPORT_USERS = "view all  userS on excell.";

    // API PARAMS
    public static final String WAREHOUSE_PARAM_USER_LOGIN = "Username/Password are required";
>>>>>>> Stashed changes
}
