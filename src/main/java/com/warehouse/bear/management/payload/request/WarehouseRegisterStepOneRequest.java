package com.warehouse.bear.management.payload.request;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRegisterStepOneRequest {
//    private String fullname;
//    private String username;
//    private String gender;
//    private String email;
//    private String password;
    private Set<String> role;

    @Size(max = 20)
    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_USERNAME_REQUIRED)
    private String username;

    @Size(max = 20)
    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_FULLNAME_REQUIRED)
    private String fullname;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_GENDER_REQUIRED)
    private String gender;

    @Size(max = 50)
    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_EMAIL_REQUIRED)
    @Email(regexp = WarehouseUserConstants.WAREHOUSE_PATTERN_EMAIL)
    private String email;

    @Size(max = 120)
    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_PASSWORD_REQUIRED)
//    @Pattern(regexp = WarehouseUserConstants.WAREHOUSE_PATTERN_PASSWORD)
    private String password;
}
