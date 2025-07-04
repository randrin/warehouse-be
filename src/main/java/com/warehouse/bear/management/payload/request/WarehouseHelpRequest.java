package com.warehouse.bear.management.payload.request;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseHelpRequest {

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_TITLE_REQUIRED)
    private String title;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_DESCRIPTION_REQUIRED)
    private String description;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_CONTENT_REQUIRED)
    private String content;

    private WarehouseStatusEnum status;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_USER_REQUIRED)
    private String userId;
}
