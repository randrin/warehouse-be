package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import com.warehouse.bear.management.exception.TokenRefreshException;
import com.warehouse.bear.management.exception.UserNotFoundException;
import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseUserInfo;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import com.warehouse.bear.management.payload.request.*;
import com.warehouse.bear.management.payload.response.WarehouseJwtResponse;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.payload.response.WarehouseTokenRefreshResponse;
import com.warehouse.bear.management.repository.WarehouseUserInfoRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.utils.WarehouseAddressRepository;
import com.warehouse.bear.management.repository.utils.WarehouseContactRepository;
import com.warehouse.bear.management.services.impl.WarehouseUserDetailsImpl;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import com.warehouse.bear.management.utils.WarehouseJwtUtil;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseAuthService {

    private AuthenticationManager authenticationManager;
    private WarehouseUserDetailsService warehouseUserDetailsService;
    private WarehouseUserRepository userRepository;
    private WarehouseAddressRepository addressRepository;
    private WarehouseContactRepository contactRepository;
    private WarehouseUserInfoRepository userInfoRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private WarehouseTokenService warehouseTokenService;
    private WarehouseJwtUtil warehouseJwtUtil;
    private WarehouseMailUtil warehouseMailUtil;
    private WarehouseCommonUtil warehouseCommonUtil;

    public ResponseEntity<Object> registerUserStepOne(WarehouseRegisterStepOneRequest request) {

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

        // Create new user's account
        String userId = null;
        do {
            userId = WarehouseCommonUtil.generateUserId();
        } while (userRepository.findByUserId(userId).isPresent());
        WarehouseUser user = new WarehouseUser(
                0L,
                userId,
                request.getUsername(),
                request.getFullname(),
                request.getGender(),
                request.getEmail(),
                null,
                bCryptPasswordEncoder.encode(request.getPassword()),
                roles,
                null,
                false,
                WarehouseCommonUtil.generateCurrentDateUtil(),
                WarehouseCommonUtil.generateCurrentDateUtil());

        userRepository.save(user);

        // Send verification email to user
        warehouseMailUtil.verificationEmail(user.getEmail(), "", WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL);
        return new ResponseEntity(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_REGISTERED), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> loginUser(WarehouseLoginRequest request) {
        String warehouseUsername = "";
        try {
            if (request.getUsername().contains("@")) {
                // Case where the user logged in with email
                final Optional<WarehouseUser> user = userRepository.findByEmail(request.getUsername());
                warehouseUsername = user.get().getUsername();
            } else {
                // Case where the user logged in with userId or Username
                if (request.getUsername().length() == 7) {
                    final Optional<WarehouseUser> userWithUserID = userRepository.findByUserId(request.getUsername());
                    warehouseUsername = userWithUserID.get().getUsername();
                } else {
                    final UserDetails userWithUsername = warehouseUserDetailsService.loadUserByUsername(request.getUsername());
                    warehouseUsername = userWithUsername.getUsername();
                }
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(warehouseUsername, request.getPassword())
            );

            WarehouseUserDetailsImpl userDetails = (WarehouseUserDetailsImpl) authentication.getPrincipal();

            // Call this to another fields non present in security
            Optional<WarehouseUser> user = userRepository.findByUsername(warehouseUsername);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            WarehouseRefreshToken warehouseRefreshToken = warehouseTokenService.createRefreshToken(user.get().getId());

            final String jwtToken = warehouseJwtUtil.generateToken(warehouseUsername);

            String downloadURl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_ENDPOINT + "/")
                    .path(user.get().getUserId())
                    .toUriString();

            Optional<WarehouseUserInfo> userInfo = userInfoRepository.findByUser(user.get());
            Optional<WarehouseAddress> address = addressRepository.findByUserId(user.get().getUserId());
            Optional<WarehouseContact> contact = contactRepository.findByUserId(user.get().getUserId());

            return new ResponseEntity<Object>(new WarehouseJwtResponse(
                    jwtToken,
                    warehouseRefreshToken.getToken(),
                    user.get().getUserId(),
                    user.get().getFullname(),
                    user.get().getGender(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    user.get().getEmailPec(),
                    roles,
                    user.get().isActive(),
                    user.get().getLastLogin(),
                    user.get().getDateOfBirth(),
                    WarehouseUserResponse.WAREHOUSE_USER_LOGGED,
                    userInfo.isPresent() ? userInfo.get() : null,
                    address.isPresent() ? address.get() : null,
                    contact.isPresent() ? contact.get() : null,
                    downloadURl,
                    user.get().getCreatedAt()),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(WarehouseUserResponse.WAREHOUSE_USER_ERROR_LOGIN, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> refreshTokenUser(WarehouseTokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return warehouseTokenService.findByToken(requestRefreshToken)
                .map(warehouseTokenService::verifyExpiration)
                .map(WarehouseRefreshToken::getUser)
                .map(user -> {
                    String token = warehouseJwtUtil.generateToken(user.getUsername());
                    return new ResponseEntity<Object>(new WarehouseTokenRefreshResponse(
                            token,
                            requestRefreshToken),
                            HttpStatus.OK);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_REFRESH_TOKEN));
    }

    public ResponseEntity<Object> verifyTokenUser(String token) {
        try {
            String username = warehouseJwtUtil.extractUsername(token);
            UserDetails userDetails = warehouseUserDetailsService.loadUserByUsername(username);
            boolean isValidToken = warehouseJwtUtil.validateToken(token, userDetails);
            if (isValidToken) {
                return new ResponseEntity<Object>(new WarehouseResponse(
                        username,
                        WarehouseUserResponse.WAREHOUSE_USER_VERIFY_TOKEN),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new WarehouseResponse(
                        token,
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_TOKEN),
                        HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseResponse(
                    token,
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_TOKEN),
                    HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<Object> logoutUser(WarehouseLogoutRequest request) {
        // TODO: Implement the logout business logic later
        try {
            WarehouseUser user = userRepository.findByUserId(request.getUserId()).get();
            user.setLastLogin(WarehouseCommonUtil.generateCurrentDateUtil());
            userRepository.save(user);
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_LOGOUT),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + request.getUserId()),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> registerUserStepThree(WarehouseRegisterRequestStepThree request, String username) {
        WarehouseAddress address = new WarehouseAddress();
        WarehouseContact contact = new WarehouseContact();
        WarehouseUserInfo userInfo = new WarehouseUserInfo();
        try {
            WarehouseUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException(
                            WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + username));
            user.setDateOfBirth(request.getDateOfBirth());

            // Set user userInfos
            userInfo.setUser(user);
            userInfo.setTemporalPassword(Boolean.FALSE);
            userInfo.setAdminUser(Boolean.FALSE);
            userInfo.setStatus(WarehouseStatusEnum.PENDING.getStatus());

            // Set user address
            address.setUserId(user.getUserId());
            address.setCountry(request.getCountry());

            // Set user contact
            contact.setUserId(user.getUserId());
            contact.setPhoneNumber(request.getPhoneNumber());
            contact.setPhonePrefix(request.getPhonePrefix());

            // Save all data in different tables
            userRepository.save(user);
            userInfoRepository.save(userInfo);
            addressRepository.save(address);
            contactRepository.save(contact);
            return new ResponseEntity(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_REGISTERED), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_UPDATE_PROFILE_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }
}



