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
import java.util.HashSet;
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

    public static String generateUserCode() {
        // Format: XXXXXX
        return RandomStringUtils.random(6, WarehouseUserConstants.WAREHOUSE_RANDOM_NUMBERS);
    }

    public static String generateTemporalPassword() {
        return RandomStringUtils.random(1, WarehouseUserConstants.WAREHOUSE_RANDOM_LETTERS).toUpperCase()
                + RandomStringUtils.random(6, WarehouseUserConstants.WAREHOUSE_RANDOM_LETTERS).toLowerCase()
                + RandomStringUtils.random(5, WarehouseUserConstants.WAREHOUSE_RANDOM_NUMBERS)
                + RandomStringUtils.random(1, WarehouseUserConstants.WAREHOUSE_RANDOM_CHARS);
    }

    public Set<WarehouseRole> generateUserRoles(Set<String> userRoles) {
        Set<String> strRoles = userRoles;
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
        return roles;
    }
}
