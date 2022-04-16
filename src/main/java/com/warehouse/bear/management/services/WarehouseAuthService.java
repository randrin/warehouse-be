package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseRoleEnum;
import com.warehouse.bear.management.exception.RoleNotFoundException;
import com.warehouse.bear.management.exception.TokenRefreshException;
import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.payload.request.WarehouseLoginRequest;
import com.warehouse.bear.management.payload.request.WarehouseLogoutRequest;
import com.warehouse.bear.management.payload.request.WarehouseRegisterRequest;
import com.warehouse.bear.management.payload.request.WarehouseTokenRefreshRequest;
import com.warehouse.bear.management.payload.response.WarehouseJwtResponse;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.payload.response.WarehouseTokenRefreshResponse;
import com.warehouse.bear.management.repository.WarehouseRoleRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.services.impl.WarehouseUserDetailsImpl;
import com.warehouse.bear.management.utils.WarehouseJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WarehouseAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WarehouseRefreshTokenService refreshTokenService;

    @Autowired
    private WarehouseJwtUtil warehouseJwtUtil;


    public ResponseEntity<Object> registerUser(WarehouseRegisterRequest request) {

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
        WarehouseUser user = new WarehouseUser(0L, request.getUsername(), request.getFullname(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), roles);

        userRepository.save(user);
        return ResponseEntity.ok(new WarehouseResponse(user, WarehouseUserResponse.WAREHOUSE_USER_REGISTERED));
    }

    public ResponseEntity<Object> loginUser(WarehouseLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        WarehouseUserDetailsImpl userDetails = (WarehouseUserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        WarehouseRefreshToken warehouseRefreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        final String jwtToken = warehouseJwtUtil.generateToken(request.getUsername());

        return new ResponseEntity<Object>(new WarehouseJwtResponse(
                jwtToken,
                warehouseRefreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                WarehouseUserResponse.WAREHOUSE_USER_LOGGED),
                HttpStatus.OK);
    }

    public ResponseEntity<Object> refreshTokenUser(WarehouseTokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(WarehouseRefreshToken::getUser)
                .map(user -> {
                    String token = warehouseJwtUtil.generateToken(user.getUsername());
                    return new ResponseEntity<Object>(new WarehouseTokenRefreshResponse(token, requestRefreshToken), HttpStatus.OK);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public ResponseEntity<Object> logoutUser(WarehouseLogoutRequest request) {
        // TODO: Implement the logout business logic later
        return new ResponseEntity<Object>(new WarehouseMessageResponse(
                WarehouseUserResponse.WAREHOUSE_USER_LOGOUT),
                HttpStatus.OK);
    }
}
