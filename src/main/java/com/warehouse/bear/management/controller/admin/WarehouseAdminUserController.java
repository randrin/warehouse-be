package com.warehouse.bear.management.controller.admin;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.admin.WarehouseAdminUserRequest;
import com.warehouse.bear.management.services.admin.WarehouseAdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_API_ADMIN_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseAdminUserController {

    @Autowired
    private WarehouseAdminUserService adminUserService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_ADMIN_INSERT_USER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_ADMIN_OPERATION_INSERT_USER)
    public ResponseEntity<Object> warehouseAdminInsertUser(@Valid @RequestBody WarehouseAdminUserRequest request) {
        return adminUserService.adminInsertUser(request);
    }
}
