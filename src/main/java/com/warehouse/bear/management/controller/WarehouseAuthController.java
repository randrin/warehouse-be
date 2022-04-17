package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.payload.request.WarehouseLoginRequest;
import com.warehouse.bear.management.payload.request.WarehouseLogoutRequest;
import com.warehouse.bear.management.payload.request.WarehouseRegisterRequest;
import com.warehouse.bear.management.payload.request.WarehouseTokenRefreshRequest;
import com.warehouse.bear.management.services.WarehouseAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
public class WarehouseAuthController {

    @Autowired
    private WarehouseAuthService warehouseAuthService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER)
    public ResponseEntity<Object> warehouseLogin(@Valid @RequestBody WarehouseLoginRequest request) {

        try {
            return warehouseAuthService.loginUser(request);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(WarehouseUserResponse.WAREHOUSE_USER_ERROR_LOGIN, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
    public ResponseEntity<Object> warehouseRegister(@Valid @RequestBody WarehouseRegisterRequest request) {
        return warehouseAuthService.registerUser(request);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REFRESH_TOKEN)
    public ResponseEntity<Object> warehouseRefreshToken(@Valid @RequestBody WarehouseTokenRefreshRequest request) {
        return warehouseAuthService.refreshTokenUser(request);
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGOUT_USER)
    public ResponseEntity<Object> warehouseLogout(@Valid @RequestBody WarehouseLogoutRequest request) {
        return warehouseAuthService.logoutUser(request);
    }
}
