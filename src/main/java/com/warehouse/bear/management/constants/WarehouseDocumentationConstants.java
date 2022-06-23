package com.warehouse.bear.management.constants;

public class WarehouseDocumentationConstants {

    // API RESPONSE
    public static final String WAREHOUSE_AUTH_API_NAME = "Warehouse Authentication/Authorization API.";
    public static final String WAREHOUSE_API_RESPONSE_400 = "User request isn't corrects.";
    public static final String WAREHOUSE_API_RESPONSE_401 = "User isn't authorized to perform this operation.";
    public static final String WAREHOUSE_API_RESPONSE_403 = "Forbidden user to perform this operation.";
    public static final String WAREHOUSE_API_RESPONSE_500 = "Internal error. Try contact your administration.";

    // API OPERATIONS
    public static final String WAREHOUSE_OPERATION_LOGIN = "Login to warehouse system.";
    public static final String WAREHOUSE_OPERATION_REGISTER = "Register to warehouse system.";
    public static final String WAREHOUSE_OPERATION_LOGOUT = "Logout to warehouse system.";
    public static final String WAREHOUSE_OPERATION_REFRESH_TOKEN = "Refresh your token to warehouse system.";
    public static final String WAREHOUSE_OPERATION_GET_ALL_USERS = "Get all users registered in warehouse system.";
    public static final String WAREHOUSE_OPERATION_UPLOAD = "File upload in warehouse system.";
    public static final String WAREHOUSE_OPERATION_DOWNLOAD = "File download in warehouse system.";
    public static final String WAREHOUSE_OPERATION_VERIFY_TOKEN = "Verify if user's token is valid to warehouse system.";
    public static final String WAREHOUSE_OPERATION_FORGOT_PASSWORD = "Send email link to user to reset our password in warehouse system.";
    public static final String WAREHOUSE_OPERATION_VERIFY_EMAIL = "Send email link to user to verify his email.";
    public static final String WAREHOUSE_OPERATION_VERIFY_LINK = "Verify the user link provided with the verify type.";
    public static final String WAREHOUSE_OPERATION_RESET_PASSWORD = "Reset your password to warehouse system.";
    public static final String WAREHOUSE_OPERATION_CHANGE_PASSWORD = "Change your password to warehouse system.";
    public static final String WAREHOUSE_OPERATION_CHANGE_STATUS = "Activate or disable user in warehouse system.";
    public static final String WAREHOUSE_OPERATION_DELETE_USER = "Delete user registered in warehouse system.";
    public static final String WAREHOUSE_OPERATION_UPDATE_USER = "Update user in warehouse system.";

    // API PARAMS
    public static final String WAREHOUSE_PARAM_USER_LOGIN = "Username/Password are required";
}
