package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.services.WarehouseFileUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = WarehouseDocumentationConstants.WAREHOUSE_API_FILES_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseFileController {

    @Autowired
    private WarehouseFileUserService warehouseFileUserService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_UPLOAD_FILE)
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_UPLOAD)
    public ResponseEntity<Object> warehouseUploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("userId") String userId,
                                                      @RequestParam("imageType") String imageType) {
        return warehouseFileUserService.saveAttachment(file, userId, imageType);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_FILE + "/{userId}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DOWNLOAD)
    public ResponseEntity<Object> warehouseDownloadFile(@PathVariable String userId) {
        return warehouseFileUserService.getAttachment(userId);
    }

    @DeleteMapping(WarehouseUserEndpoints.WAREHOUSE_DELETE_FILE + "/{userId}")
    @Operation(summary = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DELETE_FILE)
    public ResponseEntity<Object> warehouseDeleteFile(@PathVariable String userId, @RequestParam("imageType") String imageType) {
        return warehouseFileUserService.deleteAttachment(userId, imageType);
    }
}
