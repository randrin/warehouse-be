package com.warehouse.bear.management.services.impl;

import com.warehouse.bear.management.model.WarehouseImageUser;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.services.WarehouseImageUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WarehouseImageUserServiceImpl implements WarehouseImageUserService {

    @Autowired
    private WarehouseImageUserRepository warehouseImageUserRepository;

    @Override
    public WarehouseImageUser saveAttachment(MultipartFile file) throws Exception {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("....")) {
                throw new Exception("File contains wrong characters." + fileName);
            }
            WarehouseImageUser attachment = new WarehouseImageUser(fileName, file.getContentType(), file.getBytes());
            return warehouseImageUserRepository.save(attachment);
        } catch (Exception ex) {
            throw new Exception("Could not save file: " + fileName);
        }
    }

    @Override
    public WarehouseImageUser getAttachment(String fileId) throws Exception {
        return (WarehouseImageUser) warehouseImageUserRepository
                .findById(fileId)
                .orElseThrow(() -> new Exception("File not found with ID: " + fileId));
    }
}
