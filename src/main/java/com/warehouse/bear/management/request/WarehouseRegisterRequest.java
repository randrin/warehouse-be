package com.warehouse.bear.management.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRegisterRequest {

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
}
