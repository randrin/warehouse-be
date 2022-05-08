package com.warehouse.bear.management.constants;

import io.swagger.annotations.ApiResponse;

public class WarehouseDocumentationConstants {

    public static final String WAREHOUSE_AUTH_API_NAME = "Warehouse Authentification/Authorization API";
    public static final String WAREHOUSE_API_RESPONSE_400 = "Username/Password are not corrects.";
    public static final String WAREHOUSE_API_RESPONSE_401 = "User isn't authorized to perform this operation.";
    public static final String WAREHOUSE_API_RESPONSE_403 = "Forbidden user to perform this operation.";
    public static final String WAREHOUSE_API_RESPONSE_500 = "Internal error. Try contact your administration.";
    public static final String WAREHOUSE_OPERATION_LOGIN = "Login to warehouse system";
    public static final String WAREHOUSE_OPERATION_REGISTER = "Register to warehouse system";
    public static final String WAREHOUSE_OPERATION_LOGOUT = "Logout to warehouse system";
    public static final String WAREHOUSE_OPERATION_REFRESH_TOKEN = "Refresh your token to warehouse system";
    public static final String WAREHOUSE_OPERATION_GET_ALL_USERS = "Get all users registered in warehouse system";
    public static final String WAREHOUSE_OPERATION_UPLOAD = "file upload in warehouse system";
    public static final String WAREHOUSE_OPERATION_DOWNLOAD = "file download in warehouse system";
    public static final String WAREHOUSE_OPERATION_VERIFY_TOKEN = "Verify if user's token is valid to warehouse system";
    public static final String WAREHOUSE_OPERATION_FORGOT_PASSWORD = "Send email link to user to reset our password in warehouse system";
    public static final String WAREHOUSE_OPERATION_VERIFY_LINK = "Verify the user link provided with the verify type";
    public static final String WAREHOUSE_OPERATION_RESET_PASSWORD = "Reset your password to warehouse system";
}
