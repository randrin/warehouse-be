package com.warehouse.bear.management.payload.request;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseHelpRequest {

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_HELP_TITLE)
    private String title;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_HELP_DESCRIPTION)
    private String description;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_HELP_CONTENT)
    private String content;

    private WarehouseStatusEnum status;
}
