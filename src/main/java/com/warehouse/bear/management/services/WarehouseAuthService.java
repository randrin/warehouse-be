package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseUserRole;
import com.warehouse.bear.management.payload.request.WarehouseRegisterRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.repository.WarehouseRoleRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class WarehouseAuthService {

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> registerUser(WarehouseRegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(WarehouseUserConstants.ERROR_USERNAME + request.getUsername() + WarehouseUserConstants.ERROR_USED));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new WarehouseMessageResponse(WarehouseUserConstants.ERROR_EMAIL + request.getEmail() + WarehouseUserConstants.ERROR_USED));
        }

        /* WarehouseUser user = new WarehouseUser(warehouseRegisterRequest.getUsername(), warehouseRegisterRequest.getFullname(), warehouseRegisterRequest.getEmail(),
                encoder.encode(warehouseRegisterRequest.getPassword())); */

        Set<String> strRoles = request.getRole();
        Set<WarehouseRole> roles = new HashSet<>();

        if (strRoles == null) {
            WarehouseRole userRole = roleRepository.findByName(WarehouseUserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        WarehouseRole adminRole = roleRepository.findByName(WarehouseUserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
                        roles.add(adminRole);
                        break;
                    case "moderator":
                        WarehouseRole modRole = roleRepository.findByName(WarehouseUserRole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
                        roles.add(modRole);
                        break;
                    default:
                        WarehouseRole userRole = roleRepository.findByName(WarehouseUserRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(WarehouseUserConstants.ERROR_ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        // Create new user's account
        WarehouseUser user = new WarehouseUser(0L, request.getUsername(), request.getFullname(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), roles);

        //user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new WarehouseMessageResponse(request.getUsername() + WarehouseUserConstants.REGISTER_SUCCESS));
    }
}
