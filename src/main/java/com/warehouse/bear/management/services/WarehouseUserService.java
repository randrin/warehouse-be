package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.exception.UserNotFoundException;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseVerifyIdentity;
import com.warehouse.bear.management.model.admin.WarehouseAdminUser;
import com.warehouse.bear.management.payload.request.WarehouseChangePasswordRequest;
import com.warehouse.bear.management.payload.request.WarehouseResetPasswordRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.payload.response.WarehouseStatusActiveResponse;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.WarehouseVerifyIdentityRepository;
import com.warehouse.bear.management.repository.admin.WarehouseAdminUserRepository;
import com.warehouse.bear.management.repository.utils.WarehouseAddressRepository;
import com.warehouse.bear.management.repository.utils.WarehouseContactRepository;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WarehouseUserService {

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseAdminUserRepository adminUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WarehouseTokenService warehouseTokenService;

    @Autowired
    private WarehouseMailUtil warehouseMailUtil;

    @Autowired
    private WarehouseContactRepository contactRepository;

    @Autowired
    private WarehouseAddressRepository addressRepository;

    @Autowired
    private WarehouseVerifyIdentityRepository verifyIdentityRepository;

    public ResponseEntity<Object> resetPasswordUser(WarehouseResetPasswordRequest request) {
        try {
            WarehouseUser user = userRepository.findByEmail(request.getEmail()).get();
            if (user != null) {
                user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
                userRepository.save(user);
                return new ResponseEntity<Object>(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_PASSWORD_CHANGED), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + request.getEmail()),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + request.getEmail()),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> verifyLinkUser(String link, String verifyType) {
        Optional<WarehouseVerifyIdentity> user = verifyIdentityRepository.findByLinkAndVerifyType(link, verifyType);
        if (user.isPresent()) {
            return new ResponseEntity<Object>(user,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_VERIFY_LINK),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> changePassword(WarehouseChangePasswordRequest request) {
        try {
            WarehouseUser user = userRepository.findByUserId(request.getUserId()).get();
            if (bCryptPasswordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                return new ResponseEntity<Object>(new WarehouseResponse(
                        user, WarehouseUserResponse.WAREHOUSE_USER_CHANGE_PASSWORD),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_PASSWORD),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + request.getUserId()),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> activateOrDisabledUser(String userId) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(userId);
            Optional<WarehouseAdminUser> adminUser = adminUserRepository.findByUserId(userId);
            if (user.isPresent()) {
                user.get().setActive(user.get().isActive() ? Boolean.FALSE : Boolean.TRUE);
                userRepository.save(user.get());
                return new ResponseEntity<Object>(new WarehouseStatusActiveResponse(user, false, WarehouseUserResponse.WAREHOUSE_USER_CHANGE_STATUS), HttpStatus.OK);
            } else if (adminUser.isPresent()) {
                // Activate adminUser status to true,
                adminUser.get().setActive(Boolean.TRUE);
                // Switch adminUser to user in users table
                WarehouseUser newUser = new WarehouseUser(
                        0L,
                        adminUser.get().getUserId(),
                        adminUser.get().getUsername(),
                        adminUser.get().getFullname(),
                        adminUser.get().getGender(),
                        adminUser.get().getEmail(),
                        adminUser.get().getTemporaryPassword(),
                        adminUser.get().getRoles(),
                        adminUser.get().getDateOfBirth(),
                        adminUser.get().isActive(),
                        adminUser.get().getLastLogin(),
                        adminUser.get().getCreatedAt()
                );
                userRepository.save(newUser);
                // Cancel the adminUser un users_tmp
                adminUserRepository.delete(adminUser.get());
                return new ResponseEntity<Object>(new WarehouseStatusActiveResponse(newUser, true, WarehouseUserResponse.WAREHOUSE_USER_CHANGE_STATUS), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getAllUsers() {
        try {
            List<WarehouseUser> users = userRepository.findAll();
            return new ResponseEntity<Object>(users, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> forgotPasswordUser(String email) {

        Optional<WarehouseUser> user = userRepository.findByEmail(email);
        if (user.isPresent()) {

            WarehouseVerifyIdentity verifyIdentity = warehouseTokenService.createForgotPassowordLink(user.get().getUserId());
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.get().getUsername().toUpperCase());
            model.put("userId", user.get().getUserId().toUpperCase());
            model.put("link", verifyIdentity.getLink());
            model.put("verifyType", verifyIdentity.getVerifyType());
            model.put("expirationLink", verifyIdentity.getExpiryDate());
            WarehouseResponse response = warehouseMailUtil.warehouseSendMail(user.get().getEmail(), model, WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_RESET_PASSWORD);

            return new ResponseEntity<Object>(response,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + email),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> deleteUser(String userId) {
        try {
            WarehouseUser user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new UserNotFoundException(WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + userId));
            userRepository.delete(user);
            return new ResponseEntity<Object>(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_DELETED), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_DELETE_ERROR),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> findUserByUserId(String userId) {
        Optional<WarehouseUser> user = null;
        Optional<WarehouseAdminUser> adminUser = null;
        try {
            user = userRepository.findByUserId(userId);
            if (!user.isPresent()) {
                adminUser = adminUserRepository.findByUserId(userId);
            }
            return new ResponseEntity<Object>(
                    new WarehouseResponse(user.isPresent() ? user : adminUser, WarehouseUserResponse.WAREHOUSE_USER_FOUND), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + userId),
                    HttpStatus.NOT_FOUND);
        }
    }
}
