package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.payload.request.WarehouseLoginRequest;
import com.warehouse.bear.management.payload.request.WarehouseLogoutRequest;
import com.warehouse.bear.management.payload.request.WarehouseRegisterRequest;
import com.warehouse.bear.management.payload.request.WarehouseTokenRefreshRequest;
import com.warehouse.bear.management.services.WarehouseAuthService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_AUTH_API_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
public class WarehouseAuthController {

    @Autowired
    private WarehouseAuthService warehouseAuthService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_LOGIN)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Username/Password are not corrects."),
            @ApiResponse(code = 403, message = "User isn't authorized to perform operation."),
            @ApiResponse(code = 401, message = "Forbidden user to perform operation."),
            @ApiResponse(code = 500, message = "Internal error. Try contact your administration.")
    })
    public ResponseEntity<Object> warehouseLogin(
            @ApiParam(value ="Username/Password are required", required = true) @Valid @RequestBody WarehouseLoginRequest request) {

        try {
            return warehouseAuthService.loginUser(request);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(WarehouseUserResponse.WAREHOUSE_USER_ERROR_LOGIN, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REGISTER)
    public ResponseEntity<Object> warehouseRegister(@Valid @RequestBody WarehouseRegisterRequest request) {
        return warehouseAuthService.registerUser(request);
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
}
