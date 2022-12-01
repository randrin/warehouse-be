package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import com.warehouse.bear.management.exception.ObjectNotFoundException;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.utils.WarehouseHelp;
import com.warehouse.bear.management.payload.request.WarehouseHelpRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.utils.WarehouseHelpRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WarehouseHelpService {

    private WarehouseHelpRepository helpRepository;
    private WarehouseUserRepository userRepository;

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
            Optional<WarehouseUser> user = userRepository.findByUserId(request.getUserId());
            if (user != null) {
                WarehouseHelp help = new WarehouseHelp()
                        .builder()
                        .id(0L)
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .content(request.getContent())
                        .status(request.getStatus() != null ? request.getStatus() : WarehouseStatusEnum.DISABLED)
                        .user(user.get())
                        .createdAt(WarehouseCommonUtil.generateCurrentDateUtil())
                        .updatedAt(WarehouseCommonUtil.generateCurrentDateUtil())
                        .build();
                helpRepository.save(help);
                return new ResponseEntity<Object>(new WarehouseResponse(help, WarehouseUserResponse.WAREHOUSE_OBJECT_CREATED),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + request.getUserId()),
                        HttpStatus.NOT_FOUND);
            }

        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteHelp(String title) {
        try {
            WarehouseHelp help = helpRepository.findByTitle(title)
                    .orElseThrow(() -> new ObjectNotFoundException(WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + title));
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
                    .orElseThrow(() -> new ObjectNotFoundException(WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + title));
            Optional<WarehouseUser> user = userRepository.findByUserId(request.getUserId());
            help.setTitle(request.getTitle());
            help.setDescription(request.getDescription());
            help.setContent(request.getContent());
            help.setStatus(request.getStatus());
            help.setUser(user.get());
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

    public ResponseEntity<Object> changeStatusHelp(String userId, String title, String status) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(userId);
            Optional<WarehouseHelp> help = helpRepository.findByTitle(title);
            if (help != null) {
                help.get().setStatus(WarehouseStatusEnum.valueOf(status.toUpperCase()));
                help.get().setUser(user.get());
                help.get().setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                helpRepository.save(help.get());
                return new ResponseEntity<Object>(new WarehouseResponse(help, WarehouseUserResponse.WAREHOUSE_OBJECT_UPDATED), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + title),
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                    HttpStatus.NOT_FOUND);
        }
    }
}
