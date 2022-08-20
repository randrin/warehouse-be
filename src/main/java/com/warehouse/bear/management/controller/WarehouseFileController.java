package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.services.WarehouseFileUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_API_FILES_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseFileController {

    @Autowired
    private WarehouseFileUserService warehouseFileUserService;
    @Autowired
    private WarehouseUserRepository userRepository;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_UPLOAD_FILE)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_UPLOAD)
    public ResponseEntity<Object> warehouseUploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("userId") String userId,
                                                      @RequestParam("imageType") String imageType) {
        return warehouseFileUserService.saveAttachment(file, userId, imageType);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_FILE + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DOWNLOAD)
    public ResponseEntity<Object> warehouseDownloadFile(@PathVariable String userId) {
        return warehouseFileUserService.getAttachment(userId);
    }

    @DeleteMapping(WarehouseUserEndpoints.WAREHOUSE_DELETE_FILE + "/{userId}")
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DELETE_FILE)
    public ResponseEntity<Object> warehouseDeleteFile(@PathVariable String userId, @RequestParam("imageType") String imageType) {
        return warehouseFileUserService.deleteAttachment(userId, imageType);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_PDF_VIEWER)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_VIEW_PDF)
    public void warehouseGetPdfView(HttpServletResponse response) throws IOException {
         warehouseFileUserService.getPdfUserData(response);

    }
    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_USERS_EXPORTS)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_EXPORT_USERS)
    public void exportToPDF(HttpServletResponse response) throws IOException, IOException {

         warehouseFileUserService.getExcelUsersData(response);
    }


}
