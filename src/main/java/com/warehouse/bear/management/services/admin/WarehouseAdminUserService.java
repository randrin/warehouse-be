package com.warehouse.bear.management.services.admin;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseUserInfo;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import com.warehouse.bear.management.payload.request.admin.WarehouseAdminUserRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.repository.WarehouseUserInfoRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.utils.WarehouseAddressRepository;
import com.warehouse.bear.management.repository.utils.WarehouseContactRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class WarehouseAdminUserService {

    @Autowired
    private WarehouseAddressRepository addressRepository;

    @Autowired
    private WarehouseContactRepository contactRepository;

    @Autowired
    private WarehouseUserInfoRepository userInfoRepository;

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WarehouseMailUtil warehouseMailUtil;

    @Autowired
    private WarehouseCommonUtil warehouseCommonUtil;

    public ResponseEntity<Object> adminInsertUser(WarehouseAdminUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_USERNAME_EXISTS + request.getUsername()));
        }

        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByEmailPec(request.getEmail())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_EMAIL_EXISTS + request.getEmail()));
        }

        if (request.getEmailPec() != null) {
            if (userRepository.existsByEmailPec(request.getEmailPec()) || userRepository.existsByEmail(request.getEmailPec())) {
                return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_EMAIL_EXISTS + request.getEmailPec()));
            }
        }

        if (request.getEmailPec() != null) {
            if (request.getEmailPec().compareToIgnoreCase(request.getEmail()) == 0) {
                return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_BOTH_EMAIL));
            }
        }

        if ((request.getContact().getPhonePrefix() + request.getContact().getPhoneNumber())
                .compareToIgnoreCase(request.getContact().getLandlinePrefix() + request.getContact().getLandlineNumber()) == 0) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_BOTH_PHONE_NUMBER));
        }

        // Generate user roles
        Set<WarehouseRole> roles = warehouseCommonUtil.generateUserRoles(request.getRole());

        // Generate userId
        String userId = null;
        String temporalPassword = WarehouseCommonUtil.generateTemporalPassword();
        do {
            userId = WarehouseCommonUtil.generateUserId();
        } while (userRepository.findByUserId(userId).isPresent());

        // First save user added by admin in users table
        WarehouseUser adminUser = new WarehouseUser(
                0L,
                userId,
                request.getUsername(),
                request.getFullname(),
                request.getGender(),
                request.getEmail(),
                request.getEmailPec(),
                bCryptPasswordEncoder.encode(temporalPassword),
                roles,
                request.getDateOfBirth(),
                false,
                WarehouseCommonUtil.generateCurrentDateUtil(),
                WarehouseCommonUtil.generateCurrentDateUtil());
        userRepository.save(adminUser);

        // Then save user information in user_info table
        WarehouseStatusEnum status = WarehouseStatusEnum.PENDING;
        WarehouseUserInfo userInfo = new WarehouseUserInfo(
                0L,
                adminUser,
                Boolean.TRUE,
                Boolean.TRUE,
                status.getStatus(),
                Boolean.FALSE
        );
        userInfoRepository.save(userInfo);

        // Then save user address information in address table
        WarehouseAddress userAddress = new WarehouseAddress(
                0L,
                userId,
                request.getAddress().getCountry(),
                request.getAddress().getState(),
                request.getAddress().getAddressLine(),
                request.getAddress().getZipCode());
        addressRepository.save(userAddress);

        // Finally save user contact information in contact table
        WarehouseContact userContact = new WarehouseContact(
                0L,
                userId,
                request.getContact().getPhonePrefix(),
                request.getContact().getPhoneNumber(),
                request.getContact().getLandlinePrefix(),
                request.getContact().getLandlineNumber());
        contactRepository.save(userContact);

        // Send verification email to user
        warehouseMailUtil.verificationEmail(adminUser.getEmail(), temporalPassword, WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL_ADMIN_USER);
        // TODO: Send the verification email to user if the secondEmail (PEC Email Address) is populated
        return new ResponseEntity(new WarehouseResponse(adminUser, WarehouseUserResponse.WAREHOUSE_USER_REGISTERED), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> adminChangeStatusUser(String adminId, String userId, String status) {
        try {
            Optional<WarehouseUser> admin = userRepository.findByUserId(adminId);
            if (admin.get().isActive()) {
                Optional<WarehouseUser> user = userRepository.findByUserId(userId);
                Optional<WarehouseUserInfo> userInfo = userInfoRepository.findByUser(user.get());
                if (userInfo.isPresent()) {
                    userInfo.get().setStatus(status);
                    userInfoRepository.save(userInfo.get());
                    return new ResponseEntity<Object>(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_CHANGE_STATUS), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Object>(new WarehouseMessageResponse(
                            WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_ADMIN_ERROR_DISABLE),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                    HttpStatus.NOT_FOUND);
        }
    }
}
