package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.*;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.services.WarehouseAuthService;
import com.warehouse.bear.management.services.WarehouseImageUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_AUTH_API_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseAuthController {

    @Autowired
    private WarehouseAuthService warehouseAuthService;

    @Autowired
    private WarehouseImageUserRepository warehouseImageUserRepository;

    @Autowired
    private WarehouseImageUserService warehouseImageUserService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_LOGIN)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_400),
            @ApiResponse(code = 403, message = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_403),
            @ApiResponse(code = 401, message = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_401),
            @ApiResponse(code = 500, message = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_500)
    })
    public ResponseEntity<Object> warehouseLogin(
            @ApiParam(value = WarehouseDocumentationConstants.WAREHOUSE_PARAM_USER_LOGIN, required = true)
            @Valid @RequestBody WarehouseLoginRequest request) {
        return warehouseAuthService.loginUser(request);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REGISTER)
    public ResponseEntity<Object> warehouseRegisterStepOne(@Valid @RequestBody WarehouseRegisterRequest request,
                                                           @RequestParam(value = "step", required = true) int step) {
        return warehouseAuthService.registerUserStepOne(request);
    }

    @PatchMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER_STEP_THREE + "/{username}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REGISTER)
    public ResponseEntity<Object> warehouseRegisterStepThree(@Valid @RequestBody WarehouseRegisterRequestStepThree request,
                                                             @PathVariable String username,
                                                             @RequestParam(value = "step", required = true) int step) {
        return warehouseAuthService.registerUserStepThree(request, username);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REFRESH_TOKEN)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REFRESH_TOKEN)
    public ResponseEntity<Object> warehouseRefreshToken(@Valid @RequestBody WarehouseTokenRefreshRequest request) {
        return warehouseAuthService.refreshTokenUser(request);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_VERIFY_TOKEN + "/{token}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_VERIFY_TOKEN)
    public ResponseEntity<Object> warehouseVerifyToken(@PathVariable String token) {
        return warehouseAuthService.verifyTokenUser(token);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_LOGOUT_USER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_LOGOUT)
    public ResponseEntity<Object> warehouseLogout(@Valid @RequestBody WarehouseLogoutRequest request) {
        return warehouseAuthService.logoutUser(request);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_ALL_USERS)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_GET_ALL_USERS)
    public ResponseEntity<Object> warehouseGetAllUser() {
        return warehouseAuthService.allUser();
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_FORGOT_PASSWORD + "/{email}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_FORGOT_PASSWORD)
    public ResponseEntity<Object> warehouseForgotPassword(@PathVariable String email) {
        return warehouseAuthService.forgotPasswordUser(email);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_VERIFICATION_EMAIL + "/{email}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_VERIFY_EMAIL)
    public ResponseEntity<Object> warehouseVerificationEmail(@PathVariable String email) {
        return warehouseAuthService.userVerificationEmail(email);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_VERIFY_USER_LINK_TYPE)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_VERIFY_LINK)
    public ResponseEntity<Object> warehouseVerifyLink(@RequestParam(value = "link", required = true) String link,
                                                      @RequestParam(value = "verifyType", required = true) String verifyType) {
        return warehouseAuthService.verifyLinkUser(link, verifyType);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_RESET_PASSWORD)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_RESET_PASSWORD)
    public ResponseEntity<Object> warehouseResetPassword(@Valid @RequestBody WarehouseResetPasswordRequest request) {
        return warehouseAuthService.resetPasswordUser(request);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_UPLOAD_FILE)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_UPLOAD)
    public ResponseEntity<Object> warehouseUploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("userId") String userId) {
        return warehouseImageUserService.saveAttachment(file, userId);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_FILE + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DOWNLOAD)
    public ResponseEntity<Object> warehouseDownloadFile(@PathVariable String userId) {
        return warehouseImageUserService.getAttachment(userId);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_ACTIVATE_OR_DISABLED + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_CHANGE_STATUS)
    public ResponseEntity<Object> warehouseActivateOrDisabledUser(@PathVariable String userId) {
        return warehouseAuthService.activateOrDisabledUser(userId);
    }

    @DeleteMapping(WarehouseUserEndpoints.WAREHOUSE_DELETE_USER + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DELETE_USER)
    public ResponseEntity<Object> warehouseDeleteUser(@PathVariable String userId) {
        return warehouseAuthService.deleteUser(userId);
    }
}
