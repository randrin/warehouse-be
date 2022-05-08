package com.warehouse.bear.management.payload.response;

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
    private String username;
    private String email;
    private List<String> roles;
    private boolean isActive;
    private String lastLogin;
    private String dateOfBirth;
    private String message;

}
