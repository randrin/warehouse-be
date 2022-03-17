package com.warehouse.bear.management.controller;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.payload.request.WarehouseRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.warehouse.bear.management.exception.TokenRefreshException;
import com.warehouse.bear.management.model.WarehouseErole;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.payload.request.WarehouseLogoutRequest;
import com.warehouse.bear.management.payload.request.WarehouseLoginRequest;
import com.warehouse.bear.management.payload.request.WarehouseTokenRefreshRequest;
import com.warehouse.bear.management.payload.response.WarehouseJwtResponse;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseTokenRefreshResponse;
import com.warehouse.bear.management.repository.WarehouseRoleRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.warehouseSecurity.jwt.JwtUtils;
import com.warehouse.bear.management.services.WarehouseRefreshTokenService;
import com.warehouse.bear.management.services.WarehouseUserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;




@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT)
public class WarehouseAuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    WarehouseUserRepository userRepository;

    @Autowired
    WarehouseRoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    WarehouseRefreshTokenService refreshTokenService;

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody WarehouseLoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        WarehouseUserDetailsImpl userDetails = (WarehouseUserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        WarehouseRefreshToken warehouseRefreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new WarehouseJwtResponse(jwt, warehouseRefreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles, WarehouseUserConstants.LOGIN_SUCCESS + userDetails.getUsername() ));
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
    public ResponseEntity<?> registerUser(@Valid @RequestBody WarehouseRegisterRequest warehouseRegisterRequest) {
        if (userRepository.existsByUsername(warehouseRegisterRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(WarehouseUserConstants.ERROR_USERNAME + warehouseRegisterRequest.getUsername()+WarehouseUserConstants.ERROR_USED));
        }

        if (userRepository.existsByEmail(warehouseRegisterRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(WarehouseUserConstants.ERROR_EMAIL +warehouseRegisterRequest.getEmail()+WarehouseUserConstants.ERROR_USED));
        }

        // Create new user's account
        WarehouseUser user = new WarehouseUser(warehouseRegisterRequest.getUsername(),warehouseRegisterRequest.getFullname(), warehouseRegisterRequest.getEmail(),
                encoder.encode(warehouseRegisterRequest.getPassword()));

        Set<String> strRoles = warehouseRegisterRequest.getRole();
        Set<WarehouseRole> roles = new HashSet<>();

        if (strRoles == null) {
            WarehouseRole userRole = roleRepository.findByName(WarehouseErole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        WarehouseRole adminRole = roleRepository.findByName(WarehouseErole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
                        roles.add(adminRole);

                        break;
                    case "moderator":
                        WarehouseRole modRole = roleRepository.findByName(WarehouseErole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
                        roles.add(modRole);

                        break;
                    default:
                        WarehouseRole userRole = roleRepository.findByName(WarehouseErole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new WarehouseMessageResponse(warehouseRegisterRequest.getUsername() + WarehouseUserConstants.REGISTER_SUCCESS));
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_REFRESH_TOKEN)
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody WarehouseTokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(WarehouseRefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new WarehouseTokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping(WarehouseUserEndpoints.WAREHOUSE_LOGOUT_USER)
    public ResponseEntity<?> logoutUser(@Valid @RequestBody WarehouseLogoutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new WarehouseMessageResponse(WarehouseUserConstants.LOGOUT_SUCCESS));
    }

}
