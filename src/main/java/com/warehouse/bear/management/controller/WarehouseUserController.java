package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
public class WarehouseUserController {

    @Autowired
   // private WarehouseUserService userService;

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_DASHBOARD)
    public String dashboard() {
        return "Dashboard page";
    }

    @GetMapping(WarehouseUserEndpoints.WAREHOUSE_HOME)
    public String home() {
        return "HOme page";
    }
}
