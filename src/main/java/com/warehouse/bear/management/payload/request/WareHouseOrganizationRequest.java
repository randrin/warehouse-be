package com.warehouse.bear.management.payload.request;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.enums.WarehousePackageEnum;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WareHouseOrganizationRequest {

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_NAME_REQUIRED)
    private String organizationName;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_DESCRIPTION_REQUIRED)
    private String organizationDescription;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_WEBSITE_REQUIRED)
    private String website;

    private WarehouseContact contact;
    private WarehouseAddress address;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_REFERENT_REQUIRED)
    private String referent;

    private String maxNumberCollaborators;
    private WarehousePackageEnum organizationPackage;
    private boolean trial;
    private String startPackage;
    private String endPackage;
}
