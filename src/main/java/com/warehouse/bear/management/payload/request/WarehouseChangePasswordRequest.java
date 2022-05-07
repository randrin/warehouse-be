package com.warehouse.bear.management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseChangePasswordRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
