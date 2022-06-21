package com.warehouse.bear.management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseStatusActiveResponse {

    private Object object;
    private boolean isAdminUser;
    private String message;
}
