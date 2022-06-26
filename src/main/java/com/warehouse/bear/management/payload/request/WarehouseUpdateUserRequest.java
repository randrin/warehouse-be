package com.warehouse.bear.management.payload.request;

import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseUpdateUserRequest {

    private String fullname;
    private String username;
    private String gender;
    private String email;
    private String emailPec;
    private Set<String> role;
    private String dateOfBirth;
    private WarehouseAddress address;
    private WarehouseContact contact;
}




