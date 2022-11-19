package com.warehouse.bear.management.payload.response;

import com.warehouse.bear.management.model.WarehouseUserInfo;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseJwtResponse {
    private String token;
    private String refreshToken;
    private String userId;
    private String fullname;
    private String gender;
    private String username;
    private String email;
    private String emailPec;
    private List<String> roles;
    private boolean isActive;
    private String lastLogin;
    private String dateOfBirth;
    private String message;
    private WarehouseUserInfo userInfo;
    private WarehouseAddress address;
    private WarehouseContact contact;
    private String profileUrl;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
