package com.warehouse.bear.management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRegisterRequest {
    private String fullname;
    private String username;
    private String gender;
    private String email;
    private String password;
    private Set<String> role;
}
