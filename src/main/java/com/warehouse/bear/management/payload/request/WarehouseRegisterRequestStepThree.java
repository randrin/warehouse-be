package com.warehouse.bear.management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRegisterRequestStepThree {
    private String dateOfBirth;
    private String country;
    private String phoneNumber;
    private String phonePrefix;
}
