package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.exception.UserNotFoundException;
import com.warehouse.bear.management.model.*;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import com.warehouse.bear.management.payload.request.WarehouseChangePasswordRequest;
import com.warehouse.bear.management.payload.request.WarehouseResetPasswordRequest;
import com.warehouse.bear.management.payload.request.WarehouseUpdateUserRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.payload.response.WarehouseUserInfoResponse;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.repository.WarehouseUserInfoRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.WarehouseVerifyIdentityRepository;
import com.warehouse.bear.management.repository.utils.WarehouseAddressRepository;
import com.warehouse.bear.management.repository.utils.WarehouseContactRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WarehouseUserService {

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseUserInfoRepository userInfoRepository;

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

    @Autowired
    private WarehouseCommonUtil warehouseCommonUtil;

    @Autowired
    private WarehouseImageUserRepository imageUserRepository;

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

    public ResponseEntity<Object> findUserByVerifyLinkAndType(String link, String verifyType, String userId) {
        Optional<WarehouseVerifyIdentity> user = verifyIdentityRepository.findByLinkAndVerifyTypeAndUserId(link, verifyType, userId);
        if (user.isPresent()) {
            return new ResponseEntity<Object>(user,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_VERIFY_LINK),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> changePasswordUser(WarehouseChangePasswordRequest request) {
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
            if (user.isPresent()) {
                user.get().setActive(user.get().isActive() ? Boolean.FALSE : Boolean.TRUE);
                userRepository.save(user.get());
                return new ResponseEntity<Object>(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_CHANGE_STATUS), HttpStatus.OK);
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

//    public ResponseEntity<Object> findUserInfoByUserId(String userId) {
//        Optional<WarehouseUser> user = null;
//        Optional<WarehouseUserInfo> userInfo = null;
//        try {
//            user = userRepository.findByUserId(userId);
//            userInfo = userInfoRepository.findByUser(user.get());
//
//            return new ResponseEntity<Object>(
//                    new WarehouseResponse(userInfo, WarehouseUserResponse.WAREHOUSE_USER_FOUND), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<Object>(new WarehouseMessageResponse(
//                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + userId),
//                    HttpStatus.NOT_FOUND);
//        }
//    }

    public ResponseEntity<Object> updateUser(WarehouseUpdateUserRequest request, String userId) {

        WarehouseContact contact = null;
        WarehouseAddress address = null;
        try {
            WarehouseUser user = userRepository.findByUserId(userId).get();
            contact = contactRepository.findByUserId(userId).get();
            address = addressRepository.findByUserId(userId).get();
            if (user != null) {
                Set<WarehouseRole> roles = warehouseCommonUtil.generateUserRoles(request.getRole());

                // Set user model
                user.setFullname(request.getFullname());
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setEmailPec(request.getEmailPec());
                user.setDateOfBirth(request.getDateOfBirth());
                user.setRoles(roles);
                user.setGender((request.getGender()));

                // Set address model
                address.setCountry(request.getAddress().getCountry());
                address.setAddressLine(request.getAddress().getAddressLine());
                address.setState(request.getAddress().getState());
                address.setZipCode(request.getAddress().getZipCode());

                // Set contact model
                contact.setPhoneNumber(request.getContact().getPhoneNumber());
                contact.setPhonePrefix(request.getContact().getPhonePrefix());
                contact.setLandlineNumber(request.getContact().getLandlineNumber());
                contact.setLandlinePrefix(request.getContact().getLandlinePrefix());

                // Update all data in different databases
                userRepository.save(user);
                addressRepository.save(address);
                contactRepository.save(contact);
                return new ResponseEntity<Object>(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_UPDATE_PROFILE), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + userId),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_UPDATE_PROFILE_NOT_FOUND + userId),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> findUserByUserId(String userId) {
        Optional<WarehouseContact> contact = null;
        Optional<WarehouseAddress> address = null;
        WarehouseUser user = null;
        Optional<WarehouseUserInfo> userInfo = null;
        Optional<WarehouseImageUser> profileImage = null;
        try {
            user = userRepository.findByUserId(userId).get();
            contact = contactRepository.findByUserId(userId);
            address = addressRepository.findByUserId(userId);
            userInfo = userInfoRepository.findByUser(user);
            profileImage = imageUserRepository.findByUser(user);

            return new ResponseEntity<Object>(
                    new WarehouseUserInfoResponse(
                            userId,
                            user.getFullname(),
                            user.getGender(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getEmailPec(),
                            user.getRoles(),
                            user.isActive(),
                            user.getLastLogin(),
                            user.getDateOfBirth(),
                            user.getCreatedAt(),
                            userInfo.isPresent() ? userInfo.get() : null,
                            address.isPresent() ? address.get() : null,
                            contact.isPresent() ? contact.get() : null,
                            profileImage.isPresent() ? profileImage.get() : null),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + userId),
                    HttpStatus.NOT_FOUND);
        }
    }
}
