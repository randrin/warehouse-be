package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseImageUser;
import com.warehouse.bear.management.payload.request.WarehouseLoginRequest;
import com.warehouse.bear.management.payload.request.WarehouseLogoutRequest;
import com.warehouse.bear.management.payload.request.WarehouseRegisterRequest;
import com.warehouse.bear.management.payload.request.WarehouseTokenRefreshRequest;
import com.warehouse.bear.management.payload.response.WarehouseResponseDataImageUser;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.payload.request.*;
import com.warehouse.bear.management.services.WarehouseAuthService;
import com.warehouse.bear.management.services.WarehouseImageUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin("*")
@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_AUTH_API_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
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
            @ApiResponse(code = 400, message = "Username/Password are not corrects."),
            @ApiResponse(code = 403, message = "User isn't authorized to perform operation."),
            @ApiResponse(code = 401, message = "Forbidden user to perform operation."),
            @ApiResponse(code = 500, message = "Internal error. Try contact your administration.")
    })
    public ResponseEntity<Object> warehouseLogin(
            @ApiParam(value ="Username/Password are required", required = true)
            @Valid @RequestBody WarehouseLoginRequest request) {
        return warehouseAuthService.loginUser(request);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REGISTER)
    public ResponseEntity<Object> warehouseRegisterStepOne(@Valid @RequestBody WarehouseRegisterRequest request,
                                                    @RequestParam(value = "step", required = true) int step) {
        return warehouseAuthService.registerUserStepOne(request);
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
    public WarehouseResponseDataImageUser uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        WarehouseImageUser warehouseImageUser = null;
        String downloadURl = "";
        warehouseImageUser = warehouseImageUserService.saveAttachment(file);
        downloadURl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_ENDPOINT)
                .path(warehouseImageUser.getId())
                .toUriString();
        return new WarehouseResponseDataImageUser(
                warehouseImageUser.getFileName(),
                downloadURl,
                warehouseImageUser.getFileType(),
                file.getSize()
        );
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_FILE + "/{fileId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DOWNLOAD)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        WarehouseImageUser warehouseImageUser = null;
        warehouseImageUser = warehouseImageUserService.getAttachment(fileId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(warehouseImageUser.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + warehouseImageUser.getFileName() + "\"")
                .body(new ByteArrayResource(warehouseImageUser.getData()));
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_ACTIVATE_OR_DISABLED + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_CHANGE_STATUS)
    public ResponseEntity<Object> warehouseActivateOrDisabledUser(@PathVariable String userId) {
        return warehouseAuthService.activateOrDisabledUser(userId);
    }
}
