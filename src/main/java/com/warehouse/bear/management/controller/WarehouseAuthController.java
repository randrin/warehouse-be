package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.*;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.services.WarehouseAuthService;
import io.swagger.annotations.*;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_API_AUTH_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class WarehouseAuthController {

    private WarehouseAuthService warehouseAuthService;
    private WarehouseImageUserRepository warehouseImageUserRepository;

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
    public ResponseEntity<Object> warehouseRegisterStepOne(@Valid @RequestBody WarehouseRegisterStepOneRequest request,
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
}
