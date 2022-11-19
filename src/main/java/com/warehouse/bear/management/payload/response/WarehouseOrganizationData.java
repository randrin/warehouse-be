package com.warehouse.bear.management.payload.response;

import com.warehouse.bear.management.model.organization.WarehouseOrganization;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseOrganizationData {

    private WarehouseOrganization organization;
    private WarehouseAddress organizationAddress;
    private WarehouseContact organizationContact;
}
