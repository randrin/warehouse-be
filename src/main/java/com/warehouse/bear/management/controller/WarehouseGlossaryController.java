package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseDocumentationConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.payload.request.WarehouseGlossaryRequest;
import com.warehouse.bear.management.services.WarehouseGlossaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = WarehouseDocumentationConstants.WAREHOUSE_API_GlOSSARY_NAME)
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
@CrossOrigin("*")
public class WarehouseGlossaryController {

    @Autowired
    private WarehouseGlossaryService glossaryService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_INSERT_GLOSSARY)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_INSERT_OBJECT)
    public ResponseEntity<Object> warehouseInsertGlossary(@RequestBody @Valid List<WarehouseGlossaryRequest> request) {
        return glossaryService.insertGlossary(request);
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_ALL_GLOSSARIES)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_GET_ALL_OBJECTS)
    public ResponseEntity<Object> warehouseGetAllGlossaries() {
        return glossaryService.getAllGlossaries();
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_GLOSSARIES)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_GET_ALL_OBJECTS)
    public ResponseEntity<Object> warehouseGetGlossariesByObjectAndLanguage(
            @RequestParam(value = "object", required = true) String object,
            @RequestParam(value = "language", required = true) String language) {
        return glossaryService.getGlossariesByObjectAndLanguage(object, language);
    }

    @PutMapping(WarehouseUserEndpoints.WAREHOUSE_UPDATE_GLOSSARY)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_UPDATE_OBJECT)
    public ResponseEntity<Object> warehouseUpdateGlossary(
            @Valid @RequestBody WarehouseGlossaryRequest request,
            @RequestParam(value = "code", required = true) String code,
            @RequestParam(value = "language", required = true) String language) {
        return glossaryService.updateGlossary(request, code, language);
    }

    @DeleteMapping(WarehouseUserEndpoints.WAREHOUSE_DELETE_GLOSSARY)
    @ApiOperation(value = WarehouseDocumentationConstants.WAREHOUSE_OPERATION_DELETE_OBJECT)
    public ResponseEntity<Object> warehouseDeleteGlossary(
            @RequestParam(value = "code", required = true) String code) {
        return glossaryService.deleteGlossary(code);
    }
}
