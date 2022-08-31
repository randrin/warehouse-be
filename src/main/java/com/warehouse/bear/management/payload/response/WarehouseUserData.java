package com.warehouse.bear.management.payload.response;

import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseUserInfo;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseUserData {

    private WarehouseUser user;
    private WarehouseUserInfo userInfo;
    private WarehouseAddress userAddress;
    private WarehouseContact userContact;
}
