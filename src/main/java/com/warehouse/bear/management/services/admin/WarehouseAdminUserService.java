package com.warehouse.bear.management.services.admin;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
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

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_EMAIL_EXISTS + request.getEmail()));
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
        WarehouseUserInfo userInfo = new WarehouseUserInfo(
                0L,
                adminUser,
                Boolean.TRUE,
                Boolean.TRUE
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
        warehouseMailUtil.warehouseVerificationEmail(adminUser.getEmail(), temporalPassword, WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL_ADMIN_USER);
        // TODO: Send the verification email to user if the secondEmail (PEC Email Address) is populated
        return new ResponseEntity(new WarehouseResponse(adminUser, WarehouseUserResponse.WAREHOUSE_USER_REGISTERED), HttpStatus.CREATED);

    }
}
