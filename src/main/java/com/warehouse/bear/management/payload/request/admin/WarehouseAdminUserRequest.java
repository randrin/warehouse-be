package com.warehouse.bear.management.payload.request.admin;

import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseAdminUserRequest {

    private String username;
    private String fullname;
    private String gender;
    private String email;
    private String emailPec;
    private String temporaryPassword;
    private List<String> role;
    private WarehouseAddress address;
    private WarehouseContact contact;
    private String dateOfBirth;

}
