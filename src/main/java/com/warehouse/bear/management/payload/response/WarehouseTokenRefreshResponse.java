package com.warehouse.bear.management.payload.response;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseTokenRefreshResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = WarehouseUserConstants.WAREHOUSE_HEADER;

    public WarehouseTokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
