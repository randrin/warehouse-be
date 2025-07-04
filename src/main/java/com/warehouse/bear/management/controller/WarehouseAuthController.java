package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.*;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.services.WarehouseAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = WarehouseDocumentationConstants.WAREHOUSE_API_AUTH_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class WarehouseAuthController {

    private WarehouseAuthService warehouseAuthService;
    private WarehouseImageUserRepository warehouseImageUserRepository;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_LOGIN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_400),
            @ApiResponse(responseCode = "403", description = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_403),
            @ApiResponse(responseCode = "401", description = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_401),
            @ApiResponse(responseCode = "500", description = WarehouseDocumentationConstants.WAREHOUSE_API_RESPONSE_500)
    })
    public ResponseEntity<Object> warehouseLogin(
            @Parameter(description = WarehouseDocumentationConstants.WAREHOUSE_PARAM_USER_LOGIN, required = true)
            @Valid @RequestBody WarehouseLoginRequest request) {
        return warehouseAuthService.loginUser(request);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REGISTER)
    public ResponseEntity<Object> warehouseRegisterStepOne(@Valid @RequestBody WarehouseRegisterStepOneRequest request,
                                                           @RequestParam(value = "step", required = true) int step) {
        return warehouseAuthService.registerUserStepOne(request);
    }

    @PatchMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER_STEP_THREE + "/{username}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REGISTER)
    public ResponseEntity<Object> warehouseRegisterStepThree(@Valid @RequestBody WarehouseRegisterRequestStepThree request,
                                                             @PathVariable String username,
                                                             @RequestParam(name = "step", required = true) int step) {
        return warehouseAuthService.registerUserStepThree(request, username);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REFRESH_TOKEN)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_REFRESH_TOKEN)
    public ResponseEntity<Object> warehouseRefreshToken(@Valid @RequestBody WarehouseTokenRefreshRequest request) {
        return warehouseAuthService.refreshTokenUser(request);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_VERIFY_TOKEN + "/{token}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_VERIFY_TOKEN)
    public ResponseEntity<Object> warehouseVerifyToken(@PathVariable String token) {
        return warehouseAuthService.verifyTokenUser(token);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_LOGOUT_USER)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_LOGOUT)
    public ResponseEntity<Object> warehouseLogout(@Valid @RequestBody WarehouseLogoutRequest request) {
        return warehouseAuthService.logoutUser(request);
    }
}
