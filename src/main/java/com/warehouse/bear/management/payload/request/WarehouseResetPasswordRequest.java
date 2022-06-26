package com.warehouse.bear.management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResetPasswordRequest {
    private String email;
    private String password;
}
