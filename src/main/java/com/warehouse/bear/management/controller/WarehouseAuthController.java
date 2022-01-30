package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.request.WarehouseLoginRequest;
import com.warehouse.bear.management.request.WarehouseRegisterRequest;
import com.warehouse.bear.management.response.WarehouseResponse;
import com.warehouse.bear.management.service.WarehouseUserService;
import com.warehouse.bear.management.utils.WarehouseJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
public class WarehouseAuthController {

    @Autowired
    private WarehouseUserService warehouseUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WarehouseJwtUtil warehouseJwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER)
    public ResponseEntity<Object> warehouseLogin(@RequestBody WarehouseLoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            final String jwtToken = warehouseJwtUtil.generateToken(request.getUsername());
            return new ResponseEntity<Object>(new WarehouseResponse(WarehouseUserResponse.WAREHOUSE_USER_LOGGED, jwtToken), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(WarehouseUserResponse.WAREHOUSE_USER_ERROR_LOGIN, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
    public ResponseEntity<Object> warehouseRegister(@RequestBody WarehouseRegisterRequest request) {
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        return warehouseUserService.warehouseRegister(request);
    }
}
