package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import com.warehouse.bear.management.exception.UserNotFoundException;
import com.warehouse.bear.management.model.utils.WarehouseHelp;
import com.warehouse.bear.management.payload.request.WarehouseHelpRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.repository.utils.WarehouseHelpRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseHelpService {

    @Autowired
    private WarehouseHelpRepository helpRepository;

    public ResponseEntity<Object> getAllHelps() {
        try {
            List<WarehouseHelp> helps = helpRepository.findAll();
            return new ResponseEntity<Object>(helps, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> insertHelp(WarehouseHelpRequest request) {
        try {
            if (helpRepository.existsByTitle(request.getTitle())) {
                return new ResponseEntity<Object>(
                        new WarehouseMessageResponse(WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING + request.getTitle()),
                        HttpStatus.FOUND);
            }
            WarehouseHelp help = new WarehouseHelp()
                    .builder()
                    .id(0L)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .content(request.getContent())
                    .status(request.getStatus() != null ? request.getStatus() : WarehouseStatusEnum.PENDING)
                    .createdAt(WarehouseCommonUtil.generateCurrentDateUtil())
                    .updatedAt(WarehouseCommonUtil.generateCurrentDateUtil())
                    .build();
            helpRepository.save(help);
            return new ResponseEntity<Object>(new WarehouseResponse(help, WarehouseUserResponse.WAREHOUSE_OBJECT_CREATED),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteHelp(String title) {
        try {
            WarehouseHelp help = helpRepository.findByTitle(title)
                    .orElseThrow(() -> new UserNotFoundException(WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + title));
            helpRepository.delete(help);
            return new ResponseEntity<Object>(new WarehouseResponse(help, WarehouseUserResponse.WAREHOUSE_OBJECT_DELETED), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + title),
                    HttpStatus.NOT_FOUND);

        }
    }

    public ResponseEntity<Object> updateHelp(String title, WarehouseHelpRequest request) {
        try {
            WarehouseHelp help = helpRepository.findByTitle(title)
                    .orElseThrow(() -> new UserNotFoundException(WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + title));
            help.setTitle(request.getTitle());
            help.setDescription(request.getDescription());
            help.setContent(request.getContent());
            help.setStatus(request.getStatus());
            help.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
            helpRepository.save(help);
            return new ResponseEntity<Object>(new WarehouseResponse(help, WarehouseUserResponse.WAREHOUSE_OBJECT_UPDATED),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + title),
                    HttpStatus.NOT_FOUND);
        }
    }
}
