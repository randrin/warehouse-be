package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.WarehouseChangePasswordRequest;
import com.warehouse.bear.management.payload.request.WarehouseResetPasswordRequest;
import com.warehouse.bear.management.payload.request.WarehouseUpdateUserRequest;
import com.warehouse.bear.management.services.WarehouseUserService;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_API_USER_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseUserController {

    @Autowired
    private WarehouseUserService warehouseUserService;

    @Autowired
    private WarehouseMailUtil warehouseMailUtil;

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_DASHBOARD)
    public String dashboard() {
        return "Dashboard page";
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_HOME)
    public String home() {
        return "Home page";
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_ALL_USERS)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_GET_ALL_USERS)
    public ResponseEntity<Object> warehouseGetAllUsers() {
        return warehouseUserService.getAllUsers();
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_FORGOT_PASSWORD + "/{email}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_FORGOT_PASSWORD)
    public ResponseEntity<Object> warehouseForgotPassword(@PathVariable String email) {
        return warehouseUserService.forgotPasswordUser(email);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_VERIFICATION_EMAIL + "/{email}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_VERIFY_EMAIL)
    public ResponseEntity<Object> warehouseVerificationEmail(@PathVariable String email) {
        return warehouseMailUtil.warehouseVerificationEmail(email, "", WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_VERIFY_USER_LINK_TYPE)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_VERIFY_LINK)
    public ResponseEntity<Object> warehouseVerifyLink(@RequestParam(value = "link", required = true) String link,
                                                      @RequestParam(value = "verifyType", required = true) String verifyType) {
        return warehouseUserService.verifyLinkUser(link, verifyType);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_FIND_USER + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_FIND_USER_BY_LINK_TYPE)
    public ResponseEntity<Object> warehouseFindUserByVerifyLinkAndType(
            @PathVariable String userId,
            @RequestParam(value = "link", required = true) String link,
            @RequestParam(value = "verifyType", required = true) String verifyType) {
        return warehouseUserService.findUserByVerifyLinkAndType(link, verifyType, userId);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_RESET_PASSWORD)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_RESET_PASSWORD)
    public ResponseEntity<Object> warehouseResetPassword(@Valid @RequestBody WarehouseResetPasswordRequest request) {
        return warehouseUserService.resetPasswordUser(request);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_CHANGE_PASSWORD)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_CHANGE_PASSWORD)
    public ResponseEntity<Object> warehouseChangePassword(@Valid @RequestBody WarehouseChangePasswordRequest request) {
        return warehouseUserService.changePasswordUser(request);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_ACTIVATE_OR_DISABLED + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_CHANGE_STATUS)
    public ResponseEntity<Object> warehouseActivateOrDisabledUser(@PathVariable String userId) {
        return warehouseUserService.activateOrDisabledUser(userId);
    }

    @DeleteMapping(WarehouseUserEndpoints.WAREHOUSE_DELETE_USER + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DELETE_USER)
    public ResponseEntity<Object> warehouseDeleteUser(@PathVariable String userId) {
        return warehouseUserService.deleteUser(userId);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_UPDATE_USER + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_UPDATE_USER)
    public ResponseEntity<Object> warehouseUpdateUser(@Valid @RequestBody WarehouseUpdateUserRequest request,
                                                      @PathVariable String userId) {
        return warehouseUserService.updateUser(request, userId);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_FIND_USER_INFORMATION + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_FIND_USER)
    public ResponseEntity<Object> warehouseFindUserByUserId(@PathVariable String userId) {
        return warehouseUserService.findUserByUserId(userId);
    }
}
