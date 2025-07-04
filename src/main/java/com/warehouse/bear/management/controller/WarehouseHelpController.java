package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.WarehouseHelpRequest;
import com.warehouse.bear.management.services.WarehouseHelpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = WarehouseDocumentationConstants.WAREHOUSE_API_HELP_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseHelpController {

    @Autowired
    private WarehouseHelpService helpService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_INSERT_HELP)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_INSERT_OBJECT)
    public ResponseEntity<Object> warehouseInsertHelp(@RequestBody @Valid WarehouseHelpRequest request) {
        return helpService.insertHelp(request);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_ALL_HELPS)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_GET_ALL_OBJECTS)
    public ResponseEntity<Object> warehouseGetAllHelps() {
        return helpService.getAllHelps();
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_UPDATE_HELP + "/{title}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_UPDATE_OBJECT)
    public ResponseEntity<Object> warehouseUpdateHelp(@Valid @RequestBody WarehouseHelpRequest request,
                                                      @PathVariable String title) {
        return helpService.updateHelp(title, request);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_CHANGE_STATUS_HELP + "/{userId}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_CHANGE_STATUS_OBJECT)
    public ResponseEntity<Object> warehouseChangeStatusHelp(@PathVariable String userId,
                                                            @RequestParam(value = "title", required = true) String title,
                                                            @RequestParam(value = "status", required = true) String status) {
        return helpService.changeStatusHelp(userId, title, status);
    }

    @DeleteMapping(WarehouseUserEndpoints.WAREHOUSE_DELETE_HELP + "/{title}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DELETE_OBJECT)
    public ResponseEntity<Object> warehouseDeleteHelp(@PathVariable String title) {
        return helpService.deleteHelp(title);
    }
}
