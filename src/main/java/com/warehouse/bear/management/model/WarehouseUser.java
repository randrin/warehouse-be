package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class WarehouseUser implements Serializable {
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

    @Size(max = 50)
    @Email(regexp = WarehouseUserConstants.WAREHOUSE_PATTERN_EMAIL)
    private String emailPec;

    @Size(max = 120)
    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_PASSWORD_REQUIRED)
//    @Pattern(regexp = WarehouseUserConstants.WAREHOUSE_PATTERN_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<WarehouseRole> roles;

    private String dateOfBirth;
    private boolean isEnabled;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
