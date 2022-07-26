package com.warehouse.bear.management.payload.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRoleRequest {

    private String code;
    private String description;
    private String userId;
}
