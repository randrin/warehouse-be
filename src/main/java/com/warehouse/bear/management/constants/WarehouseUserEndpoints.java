package com.warehouse.bear.management.constants;

public class WarehouseUserEndpoints {

    // ROOT
    public static final String WAREHOUSE_ROOT = "/";
    public static final String WAREHOUSE_DASHBOARD = "/dashboard";
    public static final String WAREHOUSE_HOME = "/home";
    public static final String WAREHOUSE_ROOT_ENDPOINT = "/v1/warehouse";
    public static final String WAREHOUSE_DOWNLOAD_ENDPOINT = "/v1/warehouse/download";

    // ADMIN DASHBOARD
    public static final String WAREHOUSE_ADMIN_INSERT_USER = "/admin/insert/user";

    // AUTHENTICATION / AUTHORIZATION
    public static final String WAREHOUSE_LOGIN_USER = "/login";
    public static final String WAREHOUSE_UPLOAD_FILE = "/upload_file";
    public static final String WAREHOUSE_DOWNLOAD_FILE = "/download";
    public static final String WAREHOUSE_DELETE_FILE = "/delete_file";
    public static final String WAREHOUSE_LOGOUT_USER = "/logout";
    public static final String WAREHOUSE_REGISTER_USER = "/register";
    public static final String WAREHOUSE_REGISTER_USER_STEP_THREE = "/register/step-three";
    public static final String WAREHOUSE_REFRESH_TOKEN = "/refreshtoken";
    public static final String WAREHOUSE_VERIFY_TOKEN = "/verifytoken";
    public static final String WAREHOUSE_FORGOT_PASSWORD = "/forgot/password";
    public static final String WAREHOUSE_VERIFICATION_EMAIL = "/verifyemail";
    public static final String WAREHOUSE_VERIFY_USER_LINK_TYPE = "/verify/link";
    public static final String WAREHOUSE_RESET_PASSWORD = "/reset/password";
    public static final String WAREHOUSE_CHANGE_PASSWORD = "/change/password";
    public static final String WAREHOUSE_ACTIVATE_OR_DISABLED = "/change/status";
    public static final String WAREHOUSE_ALL_USERS = "/users";
    public static final String WAREHOUSE_FIND_USER = "/find";
    public static final String WAREHOUSE_FIND_USER_INFORMATION = "/find/user-information";
    public static final String WAREHOUSE_DELETE_USER = "/delete";
    public static final String WAREHOUSE_UPDATE_USER = "/update";
}
