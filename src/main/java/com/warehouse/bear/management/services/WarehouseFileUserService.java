package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseImageUser;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.payload.response.WarehouseDataResponse;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Service
public class WarehouseFileUserService {
    @Autowired
    private WarehouseImageUserRepository warehouseImageUserRepository;

    @Autowired
    private WarehouseUserRepository userRepository;

    public ResponseEntity<Object> saveAttachment(MultipartFile file, String userId) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String downloadURl = "";
        WarehouseImageUser imageUser = null;

        try {
            if (fileName.contains("....")) {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        fileName + WarehouseUserResponse.WAREHOUSE_USER_ERROR_SAVE_ATTACHMENT),
                        HttpStatus.BAD_REQUEST);
            }
            WarehouseUser user = userRepository.findByUserId(userId).get();
            Optional<WarehouseImageUser> userImageFound = warehouseImageUserRepository.findByUser(user);

            if (userImageFound.isPresent()) {
                imageUser = userImageFound.get();
                imageUser.setFileName(fileName);
                imageUser.setFileType(file.getContentType());
                imageUser.setData(file.getBytes());
                imageUser.setLastUploadDate(WarehouseCommonUtil.generateCurrentDateUtil());
                warehouseImageUserRepository.save(imageUser);
            } else {
                imageUser = new WarehouseImageUser(fileName, file.getContentType(), user, file.getBytes(), WarehouseCommonUtil.generateCurrentDateUtil());
                warehouseImageUserRepository.save(imageUser);
            }

            downloadURl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_ENDPOINT + "/")
                    .path(user.getUserId())
                    .toUriString();
            WarehouseDataResponse responseData = new WarehouseDataResponse(fileName,
                    downloadURl,
                    file.getContentType(),
                    file.getSize());
            return new ResponseEntity<Object>(new WarehouseResponse(responseData, WarehouseUserResponse.WAREHOUSE_USER_UPLOAD_PROFILE), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getAttachment(String userId) {

        WarehouseUser user = userRepository.findByUserId(userId).get();
        Optional<WarehouseImageUser> warehouseImageUser = warehouseImageUserRepository.findByUser(user);
        if (warehouseImageUser.isPresent()) {
            return new ResponseEntity<Object>(new WarehouseResponse(warehouseImageUser, WarehouseUserResponse.WAREHOUSE_USER_UPLOAD_PROFILE), HttpStatus.OK);
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(warehouseImageUser.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + warehouseImageUser.getFileName() + "\"")
//                .body(new ByteArrayResource(warehouseImageUser.getData()));
        } else {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_ATTACHMENT + userId), HttpStatus.NOT_FOUND);
        }
    }
}
