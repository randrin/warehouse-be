package com.warehouse.bear.management.payload.response;

import com.warehouse.bear.management.model.WarehouseImageUser;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUserInfo;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseUserInfoResponse {

    private String userId;
    private String fullname;
    private String gender;
    private String username;
    private String email;
    private String emailPec;
    private Set<WarehouseRole> roles;
    private boolean isActive;
    private String lastLogin;
    private String dateOfBirth;
    private String createdAt;
    private WarehouseUserInfo userInfo;
    private WarehouseAddress address;
    private WarehouseContact contact;
    private WarehouseImageUser profileImage;
}
