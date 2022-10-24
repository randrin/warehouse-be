package com.warehouse.bear.management.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
