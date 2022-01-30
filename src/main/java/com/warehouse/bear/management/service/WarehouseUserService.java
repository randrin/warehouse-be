package com.warehouse.bear.management.service;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.request.WarehouseRegisterRequest;
import com.warehouse.bear.management.response.WarehouseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WarehouseUserService {

    @Autowired
    private WarehouseUserRepository warehouseUserRepository;

    public ResponseEntity<Object> warehouseRegister(WarehouseRegisterRequest request) {
        WarehouseUser userByUsername = warehouseUserRepository.findByUsername(request.getUsername());

        if(userByUsername == null) {
            WarehouseUser userByEmail = warehouseUserRepository.findByEmail(request.getEmail());
            if(userByEmail == null) {
                WarehouseUser warehouseUser = new WarehouseUser(0L, request.getFirstname(), request.getLastname(),
                        request.getUsername(), request.getEmail(), request.getPassword(), WarehouseUserConstants.WAREHOUSE_ROLE_USER);
                warehouseUserRepository.save(warehouseUser);
                return new ResponseEntity<Object>(new WarehouseResponse(warehouseUser, WarehouseUserResponse.WAREHOUSE_USER_CREATED), HttpStatus.CREATED);
            }
            return new ResponseEntity(WarehouseUserResponse.WAREHOUSE_USER_EMAIL_EXISTS + userByEmail.getEmail(), HttpStatus.FOUND);
        }
        return new ResponseEntity(WarehouseUserResponse.WAREHOUSE_USER_USERNAME_EXISTS + userByUsername.getUsername(), HttpStatus.FOUND);
    }
}
