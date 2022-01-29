package com.warehouse.bear.management.service;

import com.warehouse.bear.management.repository.WarehouseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseUserService {

    @Autowired
    private WarehouseUserRepository warehouseUserRepository;

}
