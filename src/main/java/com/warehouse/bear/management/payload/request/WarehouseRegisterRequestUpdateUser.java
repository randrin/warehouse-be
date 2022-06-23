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
public class WarehouseRegisterRequestUpdateUser {
    @NotBlank
    @Size(min = 3, max = 20)
    private String fullname;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    private String gender;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    @Email
    private String emailPec;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Set<String> role;
    @NotBlank
    private String dateOfBirth;

    @NotBlank
    @Size(min = 10, max = 10)
    private String country;

    @NotBlank
    @Size(min = 10, max = 10)
    private String state;

    @NotBlank
    private String phoneNumber;


    private String address;
    private String zipCode;

    @NotBlank
    private String landlinePrefix;

    @NotBlank
    private String landlineNumber;
}




