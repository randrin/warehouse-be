package com.warehouse.bear.management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRegisterRequestStepThree {
    @NotBlank
    private String dateOfBirth;

    @NotBlank
    @Size(min = 10, max = 10)
    private String country;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String phonePrefix;

}
