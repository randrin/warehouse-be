package com.warehouse.bear.management.services.admin;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.payload.request.admin.WarehouseRoleRequest;
import com.warehouse.bear.management.repository.WarehouseRoleRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class WarehouseRoleService {

    @Autowired
    private WarehouseRoleRepository roleRepository;

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseCommonUtil warehouseCommonUtil;

    public ResponseEntity<Object> insertRole(WarehouseRoleRequest request) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(request.getUserId());
            if (user.isPresent()) {
                WarehouseRole role = new WarehouseRole(
                        0L,
                        WarehouseUserConstants.WAREHOUSE_PREFIX_ROLE + request.getCode().toUpperCase(Locale.ROOT),
                        request.getDescription(),
                        user.get(),
                        warehouseCommonUtil.generateCurrentDateUtil(),
                        warehouseCommonUtil.generateCurrentDateUtil());
                roleRepository.save(role);
                return new ResponseEntity<Object>(
                        WarehouseUserResponse.WAREHOUSE_INSERT_ROLE_SUCCESS, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + request.getUserId(),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(WarehouseUserResponse.WAREHOUSE_INSERT_ROLE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
