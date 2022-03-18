package com.warehouse.bear.management.payload.response;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseJwtResponse {
    private String token;
    private String type = WarehouseUserConstants.BEARER;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String message;

    public WarehouseJwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles, String message) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.message = message;
    }
}
