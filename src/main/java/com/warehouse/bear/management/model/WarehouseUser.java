package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class WarehouseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String userId;

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
    @Pattern(regexp = WarehouseUserConstants.WAREHOUSE_PATTERN_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<WarehouseRole> roles = new HashSet<>();

    private String dateOfBirth;
    private String phoneNumber;
    private String country;
    private String imageUrl;
    private boolean isActive;
    private String lastLogin;



}
