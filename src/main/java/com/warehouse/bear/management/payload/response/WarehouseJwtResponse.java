package com.warehouse.bear.management.payload.response;

import com.warehouse.bear.management.model.WarehouseImageUser;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseJwtResponse {
    private String token;
    private String refreshToken;
    private String userId;
    private String fullname;
    private String gender;
    private String username;
    private String email;
    private List<String> roles;
    private boolean isActive;
    private String lastLogin;
    private String dateOfBirth;
    private String message;
    private String phonePrefix;
    private String phoneNumber;
    private String country;
    private String profileUrl;
}
