package com.warehouse.bear.management.payload.request;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseGlossaryRequest {

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_CODE_REQUIRED)
    private String code;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_DESCRIPTION_REQUIRED)
    private String description;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_LANGUAGE_REQUIRED)
    private String language;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_OBJECT_REQUIRED)
    private String object;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_USER_REQUIRED)
    private String userId;
}
