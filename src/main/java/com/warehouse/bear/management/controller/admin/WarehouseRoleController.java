package com.warehouse.bear.management.controller.admin;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.WarehouseResetPasswordRequest;
import com.warehouse.bear.management.payload.request.admin.WarehouseRoleRequest;
import com.warehouse.bear.management.services.admin.WarehouseRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_API_ROLE_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseRoleController {

    @Autowired
    private WarehouseRoleService roleService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_ADD_ROLE)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_INSERT_ROLE)
    public ResponseEntity<Object> warehouseInsertRole(@Valid @RequestBody WarehouseRoleRequest request) {
        return roleService.insertRole(request);
    }
}
