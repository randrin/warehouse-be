package com.warehouse.bear.management.services;

import com.warehouse.bear.management.model.WarehouseImageUser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface WarehouseImageUserService {
    WarehouseImageUser saveAttachment(MultipartFile file) throws Exception;

    WarehouseImageUser getAttachment(String fileId) throws Exception;
}
