package com.warehouse.bear.management.utils;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehouseRoleEnum;
import com.warehouse.bear.management.exception.RoleNotFoundException;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.repository.WarehouseRoleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WarehouseCommonUtil {

    @Autowired
    private WarehouseRoleRepository roleRepository;

    public static String generateCurrentDateUtil() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WarehouseUserConstants.WAREHOUSE_PATTERN_DATE);
        return dtf.format(LocalDateTime.now());
    }

    public static String generateUserId() {
        // Format: XXYYYYY
        return RandomStringUtils.random(2, WarehouseUserConstants.WAREHOUSE_RANDOM_LETTERS).toUpperCase()
                + RandomStringUtils.random(5, WarehouseUserConstants.WAREHOUSE_RANDOM_NUMBERS);
    }

    public static String generateTemporalPassword() {
        return RandomStringUtils.random(1, WarehouseUserConstants.WAREHOUSE_RANDOM_LETTERS).toUpperCase()
                + RandomStringUtils.random(6, WarehouseUserConstants.WAREHOUSE_RANDOM_LETTERS).toLowerCase()
                + RandomStringUtils.random(5, WarehouseUserConstants.WAREHOUSE_RANDOM_NUMBERS)
                + RandomStringUtils.random(1, WarehouseUserConstants.WAREHOUSE_RANDOM_CHARS);
    }

    public List<WarehouseRole> generateUserRoles(List<String> userRoles) {
        List<String> strRoles = userRoles;
        List<WarehouseRole> roles = new ArrayList<>();

        if (strRoles == null) {
            WarehouseRole userRole = roleRepository.findByCode(WarehouseUserConstants.WAREHOUSE_ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                roles.add(roleRepository.findByCode(role).get());
//                switch (role) {
//                    case "admin":
//                        WarehouseRole adminRole = roleRepository.findByCode(WarehouseRoleEnum.ROLE_ADMIN)
//                                .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
//                        roles.add(adminRole);
//                        break;
//                    case "moderator":
//                        WarehouseRole modRole = roleRepository.findByCode(WarehouseRoleEnum.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
//                        roles.add(modRole);
//                        break;
//                    default:
//                        WarehouseRole userRole = roleRepository.findByCode(WarehouseRoleEnum.ROLE_USER)
//                                .orElseThrow(() -> new RoleNotFoundException(WarehouseUserResponse.WAREHOUSE_ROLE_NOT_FOUND));
//                        roles.add(userRole);
//                }
            });
        }
        return roles;
    }
}
