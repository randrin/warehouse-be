package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseRoleEnum;
import com.warehouse.bear.management.exception.RoleNotFoundException;
import com.warehouse.bear.management.exception.TokenRefreshException;
import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseVerifyIdentity;
import com.warehouse.bear.management.payload.request.*;
import com.warehouse.bear.management.payload.response.WarehouseJwtResponse;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.payload.response.WarehouseTokenRefreshResponse;
import com.warehouse.bear.management.repository.WarehouseRoleRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.WarehouseVerifyIdentityRepository;
import com.warehouse.bear.management.services.impl.WarehouseUserDetailsImpl;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import com.warehouse.bear.management.utils.WarehouseJwtUtil;
import com.warehouse.bear.management.utils.WarehouseMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarehouseAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WarehouseUserDetailsService warehouseUserDetailsService;

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseRoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WarehouseTokenService warehouseTokenService;

    @Autowired
    private WarehouseJwtUtil warehouseJwtUtil;

    @Autowired
    private WarehouseMailUtil warehouseMailUtil;

    @Autowired
    private WarehouseVerifyIdentityRepository verifyIdentityRepository;


    public ResponseEntity<Object> registerUserStepOne(WarehouseRegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_USERNAME_EXISTS + request.getUsername()));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_EMAIL_EXISTS + request.getEmail()));
        }


        Set<String> strRoles = request.getRole();
        Set<WarehouseRole> roles = new HashSet<>();

        if (strRoles == null) {
            WarehouseRole userRole = roleRepository.findByName(WarehouseRoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        WarehouseRole adminRole = roleRepository.findByName(WarehouseRoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
                        roles.add(adminRole);
                        break;
                    case "moderator":
                        WarehouseRole modRole = roleRepository.findByName(WarehouseRoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
                        roles.add(modRole);
                        break;
                    default:
                        WarehouseRole userRole = roleRepository.findByName(WarehouseRoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        // Create new user's account
        // TODO: Business logic to generate the userId
        String userId = null;
        do {
            userId = WarehouseCommonUtil.generateUserId();
        } while (userRepository.findByUserId(userId).isPresent());
        WarehouseUser user = new WarehouseUser(
                0L,
                userId,
                request.getUsername(),
                request.getFullname(),
                request.getEmail(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                roles,
                false,
                WarehouseCommonUtil.generateCurrentDateUtil(),
                "");

        userRepository.save(user);
        return new ResponseEntity(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_REGISTERED), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> loginUser(WarehouseLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        WarehouseUserDetailsImpl userDetails = (WarehouseUserDetailsImpl) authentication.getPrincipal();

        // Call this to another fields non present in security
        WarehouseUser user = userRepository.findByUsername(request.getUsername()).get();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        WarehouseRefreshToken warehouseRefreshToken = warehouseTokenService.createRefreshToken(userDetails.getId());

        final String jwtToken = warehouseJwtUtil.generateToken(request.getUsername());

        return new ResponseEntity<Object>(new WarehouseJwtResponse(
                jwtToken,
                warehouseRefreshToken.getToken(),
                user.getUserId(),
                user.getFullname(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                user.isActive(),
                user.getLastLogin(),
                user.getDateOfBirth(),
                WarehouseUserResponse.WAREHOUSE_USER_LOGGED),
                HttpStatus.OK);
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

    public ResponseEntity<Object> allUser() {
        try {
            List<WarehouseUser> user = userRepository.findAll();
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID),
                    HttpStatus.NOT_FOUND);
        }
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
            WarehouseResponse response = warehouseMailUtil.warehouseSendMail(user.get(), model);

            return new ResponseEntity<Object>(response,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + email),
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

    public ResponseEntity<Object> resetPassword(WarehouseResetPasswordRequest request) {

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
}
