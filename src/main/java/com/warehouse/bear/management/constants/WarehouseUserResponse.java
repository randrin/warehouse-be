package com.warehouse.bear.management.constants;

public class WarehouseUserResponse {
    public static final String WAREHOUSE_USER_CREATED = "User created successfully.";
    public static final String WAREHOUSE_OBJECT_EXISTING = "Object already exists with property ";
    public static final String WAREHOUSE_OBJECT_CREATED = "Object created successfully.";
    public static final String WAREHOUSE_OBJECT_UPDATED = "Object updated successfully.";
    public static final String WAREHOUSE_OBJECT_DELETED = "Object deleted successfully.";
    public static final String WAREHOUSE_USER_USERNAME_EXISTS = "User already exists with username ";
    public static final String WAREHOUSE_EXISTING_USER = "User already exists with user Id:  ";
    public static final String WAREHOUSE_USER_EMAIL_EXISTS = "User already exists with email ";
    public static final String WAREHOUSE_USER_PHONE_NUMBER_EXISTS = "User already exists with this phone number ";
    public static final String WAREHOUSE_USER_BOTH_EMAIL = "Email and Pec Email are the same. Please, Change another one and try again.";
    public static final String WAREHOUSE_USER_BOTH_PHONE_NUMBER = "The Phone and Landline are the same. Please, Change another one and try again.";
    public static final String WAREHOUSE_USER_LANDLINE_NUMBER_EXISTS = "User already exists with this landline number ";
    public static final String WAREHOUSE_USER_EMAIL_PEC = "User already exists with this email pec ";
    public static final String WAREHOUSE_USER_EMAIL_PEC_VERIFICATION = "Your email pec is not verify, please check your email and provide it.";
    public static final String WAREHOUSE_USER_LOGGED = "User logged successfully.";
    public static final String WAREHOUSE_USER_REGISTERED = "User registered successfully.";
    public static final String WAREHOUSE_USER_UPDATE = "User profile updated successfully.";
    public static final String WAREHOUSE_USER_LOGOUT = "User logout successfully.";
    public static final String WAREHOUSE_USER_DELETED = "User deleted successfully.";
    public static final String WAREHOUSE_ORGANIZATION_DELETED = "Organization deleted successfully.";
    public static final String WAREHOUSE_USER_FOUND = "User found successfully.";
    public static final String WAREHOUSE_ORGANIZATION_FOUND = "Organization found successfully.";
    public static final String WAREHOUSE_ORGANIZATION_UPDATED = "Organization successfully updated.";
    public static final String WAREHOUSE_ORGANIZATION_ASSIGNED_COLLABORATORS = "Collaborators successfully assigned to organization.";
    public static final String WAREHOUSE_USER_PASSWORD_GENERATED = "User Password generated successfully.";
    public static final String WAREHOUSE_USER_PASSWORD_CHANGED = "User Password changed successfully.";
    public static final String WAREHOUSE_USER_ACTIVATED = "User activated successfully.";
    public static final String WAREHOUSE_USER_DISABLED = "User disabled successfully.";
    public static final String WAREHOUSE_USER_UPDATE_PROFILE = "User Profile update successfully.";
    public static final String WAREHOUSE_USER_REFRESH_TOKEN = "Refresh token was expired. Please make a new signing request.";
    public static final String WAREHOUSE_USER_VERIFY_TOKEN = "Your token is valid and not expired. Good success in your job.";
    public static final String WAREHOUSE_USER_RESET_PASSWORD = "Confirmation reset link for your password is successfully send to ";
    public static final String WAREHOUSE_USER_VERIFICATION_EMAIL = "Confirmation link for verify your email was send to ";
    public static final String WAREHOUSE_USER_CHANGE_PASSWORD = "User Password changed successfully.";
    public static final String WAREHOUSE_USER_CHANGE_STATUS = "User status changed successfully.";
    public static final String WAREHOUSE_USER_UPLOAD_PROFILE = "Upload profile file is done successfully.";
    public static final String WAREHOUSE_USER_DELETE_PROFILE = "Profile file is deleted successfully.";
    public static final String WAREHOUSE_USER_VIEW_PDF = "data view pdf";
    public static final String WAREHOUSE_USER_CODE_SEND = "Confirmation code for complete the operation is successfully send to ";
    public static final String WAREHOUSE_USER_CODE_OK = "User code verification is correct.";
    public static final String WAREHOUSE_USER_CODE_KO = "User code verification is expired. Try resend the request against to complete the operation.";



    // ERRORS USER
    public static final String WAREHOUSE_USER_UPDATE_PROFILE_NOT_FOUND = "User Profile %s can't be updated.";
    public static final String WAREHOUSE_USER_DELETE_ERROR = "Something wrong bad. User hasn't deleted successfully.";
    public static final String WAREHOUSE_ORGANIZATION_DELETE_ERROR = "Something wrong bad. Organization hasn't deleted successfully.";
    public static final String WAREHOUSE_USER_GENERIC_ERROR = "Generic Error. Something wrong/happen bad on the system. Please, try again later.";
    public static final String WAREHOUSE_USER_ERROR_LOGIN = "Bad Credentials Username/Email/UserID and (or) Password incorrect(s).";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID = "User not found with userId ";
    public static final String WAREHOUSE_ORGANIZATION_ERROR_NOT_FOUND_WITH_ID = "Organization not found with organizationId ";
    public static final String WAREHOUSE_OBJECT_ERROR_NOT_FOUND = "Object not found with property ";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME = "User not found with username/email/userID ";
    public static final String WAREHOUSE_USER_ERROR_VERIFICATION_EMAIL = "We have some problems for the verification email ";
    public static final String WAREHOUSE_ROLE_NOT_FOUND = "Role(s) not found in the system. Contact your administration.";
    public static final String WAREHOUSE_ERROR_ALREADY_USED = " is already used, please try another one";
    public static final String WAREHOUSE_USER_ERROR_PASSWORD = "The previous old user password isn't correct. Try again !!";
    public static final String WAREHOUSE_USER_ERROR_PASSWORD_NOT_MATCH = "User old password isn't correct. Try again !!";
    public static final String WAREHOUSE_USER_ERROR_PASSWORD_OPERATION = "The password provided to perform the operation isn't correct. Try again !!";
    public static final String WAREHOUSE_USER_ERROR_DISABLE = "Your account isn't active. Contact your administration department for more information.";
    public static final String WAREHOUSE_USER_NOT_PRESENT = "We don't have any user registered yet.";
    public static final String WAREHOUSE_USER_ERROR_TOKEN = "Your token is expired or not valid. Please try login again.";
    public static final String WAREHOUSE_USER_ERROR_REFRESH_TOKEN = "The refresh token that you sending isn't not present in database. Try again";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_VERIFY_LINK = "Link and verify type that you are provided aren't corrects. Try resend the new link to complete the operation";
    public static final String WAREHOUSE_USER_ERROR_WRONG_ATTACHMENT = "Could not save this attachment ";
    public static final String WAREHOUSE_USER_ERROR_SAVE_ATTACHMENT = " File contains wrong characters. Try another one.";
    public static final String WAREHOUSE_USER_ERROR_NOT_FOUND_ATTACHMENT = "User file not found with userID: ";
    public static final String WAREHOUSE_USER_ERROR_VIEW_PDF = "error on view pdf";
    public static final String WAREHOUSE_USER_ERROR_CODE = "The code provided to verify your request isn't correct. Try another code.";


    // ERRORS ADMIN
    public static final String WAREHOUSE_ADMIN_ERROR_DISABLE  = "Admin role isn't active. Contact your administration head department for more information.";
}
