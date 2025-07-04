package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.enums.WarehousePackageEnum;
import com.warehouse.bear.management.payload.request.WareHouseOrganizationRequest;
import com.warehouse.bear.management.services.WarehouseOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = WarehouseDocumentationConstants.WAREHOUSE_API_ORGANIZATION_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseOrganizationController {

    @Autowired
    private WarehouseOrganizationService organizationService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_INSERT_ORGANIZATION)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_INSERT_OBJECT)
    public ResponseEntity<Object> warehouseInsertOrganization(@RequestBody @Valid WareHouseOrganizationRequest request) {
        return organizationService.insertOrganization(request);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_ALL_ORGANIZATIONS)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_GET_ALL_ORGANIZATIONS)
    public ResponseEntity<Object> warehouseGetAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_FIND_ORGANIZATION + "/{organizationId}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_FIND_ORGANIZATION)
    public ResponseEntity<Object> warehouseFindOrganization(@PathVariable String organizationId) {
        return organizationService.findOrganization(organizationId);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_CHANGE_PACKAGE_ORGANIZATION + "/{organizationId}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_CHANGE_PACKAGE_ORGANIZATION)
    public ResponseEntity<Object> warehouseChangePackageOrganization(
            @PathVariable String organizationId,
            @RequestParam(value = "package", required = true) WarehousePackageEnum packageOrganization) {
        return organizationService.changePackageOrganization(organizationId, packageOrganization);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_ASSIGNED_COLLABORATORS_ORGANIZATION + "/{organizationId}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_ASSIGNED_COLLABORATORS_ORGANIZATION)
    public ResponseEntity<Object> warehouseAssignCollaboratorsToOrganization(
            @PathVariable String organizationId, @Valid @RequestBody List<String> collaborators) {
        return organizationService.assignCollaboratorsToOrganization(organizationId, collaborators);
    }

    @DeleteMapping(WarehouseUserEndpoints.WAREHOUSE_DELETE_ORGANIZATION + "/{organizationId}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DELETE_ORGANIZATION)
    public ResponseEntity<Object> warehouseDeleteOrganization(@PathVariable String organizationId) {
        return organizationService.deleteOrganization(organizationId);
    }


}
