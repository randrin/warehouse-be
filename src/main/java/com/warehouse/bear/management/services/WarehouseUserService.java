package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import com.warehouse.bear.management.exception.UserNotFoundException;
import com.warehouse.bear.management.model.*;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import com.warehouse.bear.management.payload.request.WarehouseChangePasswordRequest;
import com.warehouse.bear.management.payload.request.WarehouseResetPasswordRequest;
import com.warehouse.bear.management.payload.request.WarehouseUpdateUserRequest;
import com.warehouse.bear.management.payload.request.WarehouseVerifyCodeRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.payload.response.WarehouseUserData;
import com.warehouse.bear.management.payload.response.WarehouseUserInfoResponse;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.repository.WarehouseUserInfoRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.WarehouseVerifyIdentityRepository;
import com.warehouse.bear.management.repository.utils.WarehouseAddressRepository;
import com.warehouse.bear.management.repository.utils.WarehouseContactRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseUserService {

    private WarehouseUserRepository userRepository;
    private WarehouseUserInfoRepository userInfoRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private WarehouseTokenService warehouseTokenService;
    private WarehouseMailUtil warehouseMailUtil;
    private WarehouseContactRepository contactRepository;
    private WarehouseAddressRepository addressRepository;
    private WarehouseVerifyIdentityRepository verifyIdentityRepository;
    private WarehouseCommonUtil warehouseCommonUtil;
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
                user.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
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
                user.get().setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
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
            List<WarehouseUserData> users = userRepository.findAll()
                    .stream().map(user -> new WarehouseUserData(user,
                            userInfoRepository.findByUser(user).get(),
                            addressRepository.findByUserId(user.getUserId()).get(),
                            contactRepository.findByUserId(user.getUserId()).get())
                    ).collect(Collectors.toList());
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
            user.setDeletedAt(WarehouseCommonUtil.generateCurrentDateUtil());
            user.setActive(Boolean.FALSE);
            userRepository.save(user);
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
        WarehouseUser user = null;
        try {
            user = userRepository.findByUserId(userId).get();
            contact = contactRepository.findByUserId(userId).get();
            address = addressRepository.findByUserId(userId).get();
            if (user != null) {
                if (user.getUsername().compareToIgnoreCase(request.getUsername()) != 0) {
                    if (userRepository.existsByUsername(request.getUsername())) {
                        return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                                WarehouseUserResponse.WAREHOUSE_USER_USERNAME_EXISTS + request.getUsername()));
                    }
                }

                if (request.getEmailPec() != null) {
                    if (user.getEmailPec().compareToIgnoreCase(request.getEmailPec()) != 0) {
                        if (userRepository.existsByEmailPec(request.getEmailPec()) || userRepository.existsByEmail(request.getEmailPec())) {
                            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                                    WarehouseUserResponse.WAREHOUSE_USER_EMAIL_PEC + request.getEmailPec()));
                        }
                    }
                }

                if (contact.getPhoneNumber().compareToIgnoreCase(request.getContact().getPhoneNumber()) != 0 &&
                        contact.getPhonePrefix().compareToIgnoreCase(request.getContact().getPhonePrefix()) != 0) {
                    if (contactRepository.existsByPhoneNumber(request.getContact().getPhoneNumber()) &&
                            contactRepository.existsByPhonePrefix(request.getContact().getPhonePrefix())) {
                        return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                                WarehouseUserResponse.WAREHOUSE_USER_PHONE_NUMBER_EXISTS + request.getContact().getPhonePrefix()
                                        + " " + request.getContact().getPhoneNumber()));
                    }
                }

                if (!request.getContact().getLandlineNumber().isEmpty() && !request.getContact().getLandlinePrefix().isEmpty()) {
                    if (contact.getLandlineNumber().compareToIgnoreCase(request.getContact().getLandlineNumber()) != 0 &&
                            contact.getLandlinePrefix().compareToIgnoreCase(request.getContact().getLandlinePrefix()) != 0) {
                        if (contactRepository.existsByLandlineNumber(request.getContact().getLandlineNumber()) &&
                                contactRepository.existsByLandlinePrefix(request.getContact().getLandlinePrefix())) {
                            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                                    WarehouseUserResponse.WAREHOUSE_USER_LANDLINE_NUMBER_EXISTS + request.getContact().getLandlinePrefix()
                                            + " " + request.getContact().getLandlineNumber()));
                        }
                    }
                }

                if ((request.getContact().getPhonePrefix() + request.getContact().getPhoneNumber())
                        .compareToIgnoreCase(request.getContact().getLandlinePrefix() + request.getContact().getLandlineNumber()) == 0) {
                    return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                            WarehouseUserResponse.WAREHOUSE_USER_BOTH_PHONE_NUMBER));
                }

                Set<WarehouseRole> roles = warehouseCommonUtil.generateUserRoles(request.getRole());

                // Set user model
                user.setFullname(request.getFullname());
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setEmailPec(request.getEmailPec());
                user.setDateOfBirth(request.getDateOfBirth());
                user.setRoles(roles);
                user.setGender((request.getGender()));
                user.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());

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

                // Before saving user and information, send the verification email to email pec if populated
                if (user.getEmailPec() != null) {
                    warehouseMailUtil.verificationEmail(user.getEmailPec(), "", WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL_PEC);
                }

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
                    String.format(WarehouseUserResponse.WAREHOUSE_USER_UPDATE_PROFILE_NOT_FOUND, userId)),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> findUserByUserId(String userId) {
        Optional<WarehouseContact> contact = null;
        Optional<WarehouseAddress> address = null;
        WarehouseUser user = null;
        Optional<WarehouseUserInfo> userInfo = null;
        Optional<List<WarehouseImageUser>> profileImage = null;
        try {
            user = userRepository.findByUserId(userId).get();
            contact = contactRepository.findByUserId(userId);
            address = addressRepository.findByUserId(userId);
            userInfo = userInfoRepository.findByUser(user);
            profileImage = imageUserRepository.findProfileUser(user);

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

    public ResponseEntity<Object> updateEmailPecUser(String emailPec, String userId) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(userId);
            if (user.isPresent()) {
                if (!userRepository.existsByEmailPec(emailPec) && !userRepository.existsByEmail(emailPec)) {
                    ResponseEntity<Object> response = warehouseMailUtil.warehouseVerificationObject(
                            user.get().getEmail(),
                            emailPec,
                            warehouseCommonUtil.generateUserCode(),
                            WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL_PEC);
                    user.get().setEmailPec(emailPec);
                    user.get().setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                    userRepository.save(user.get());
                    return new ResponseEntity<Object>(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_CODE_SEND + emailPec), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Object>(new WarehouseMessageResponse(
                            WarehouseUserResponse.WAREHOUSE_USER_EMAIL_EXISTS + emailPec),
                            HttpStatus.FOUND);
                }
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + emailPec),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> verificationCode(WarehouseVerifyCodeRequest request, String userId) {
        try {
            Optional<WarehouseVerifyIdentity> user = verifyIdentityRepository.findByCodeAndVerifyType(request.getCode(), request.getVerifyType());
            if (user.get().getUserId().compareToIgnoreCase(userId) == 0) {
                LocalDateTime expiredDate = user.get().getExpiryDate();
                LocalDateTime now = LocalDateTime.now();
                boolean isBefore = now.isBefore(expiredDate);
                if (isBefore) {
                    return new ResponseEntity<Object>(new WarehouseResponse(Boolean.TRUE, WarehouseUserResponse.WAREHOUSE_USER_CODE_OK), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Object>(new WarehouseResponse(Boolean.FALSE, WarehouseUserResponse.WAREHOUSE_USER_CODE_KO), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_CODE),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateUserInfoByOperationType(String userId, String operationType) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(userId);
            Optional<WarehouseUserInfo> userInfo = userInfoRepository.findByUser(user.get());
            if (user.isPresent()) {
                if (operationType.compareToIgnoreCase(WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL_PEC) == 0) {
                    userInfo.get().setEmailPecVerified(userInfo.get().isEmailPecVerified() ? Boolean.FALSE : Boolean.TRUE);
                    user.get().setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                    userInfoRepository.save(userInfo.get());
                }
                return new ResponseEntity<Object>(new WarehouseResponse(userInfo, WarehouseUserResponse.WAREHOUSE_USER_UPDATE), HttpStatus.OK);
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

    public ResponseEntity<Object> sendCodeVerification(String userId, String operationType) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(userId);
            if (user.isPresent()) {
                ResponseEntity<Object> response = warehouseMailUtil.warehouseVerificationObject(
                        user.get().getEmail(),
                        user.get().getEmail(),
                        warehouseCommonUtil.generateUserCode(),
                        operationType);
                return new ResponseEntity<Object>(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_CODE_SEND + user.get().getEmail()), HttpStatus.OK);
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

    public ResponseEntity<Object> changeStatusUser(String userId, String status, String passwordUser) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(userId);
            if (user.get().getUserId().compareToIgnoreCase(passwordUser) == 0) {
                Optional<WarehouseUserInfo> userInfo = userInfoRepository.findByUser(user.get());
                if(status.compareToIgnoreCase(WarehouseStatusEnum.DELETED.getStatus()) == 0) {
                    userInfo.get().setDeleteDate(LocalDateTime.now().plusDays(WarehouseUserConstants.WAREHOUSE_EXPIRATION_DAYS_TO));
                } else {
                    userInfo.get().setDeleteDate(null);
                }
                userInfo.get().setStatus(status);
                user.get().setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                userRepository.save(user.get());
                userInfoRepository.save(userInfo.get());
                return new ResponseEntity<Object>(new WarehouseResponse(userInfo, WarehouseUserResponse.WAREHOUSE_USER_CHANGE_STATUS), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_PASSWORD_OPERATION),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                    HttpStatus.NOT_FOUND);
        }
    }
}
