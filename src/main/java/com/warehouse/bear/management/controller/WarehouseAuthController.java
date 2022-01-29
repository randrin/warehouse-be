package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.service.WarehouseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
public class WarehouseAuthController {

    @Autowired
    private WarehouseUserService userService;
}
