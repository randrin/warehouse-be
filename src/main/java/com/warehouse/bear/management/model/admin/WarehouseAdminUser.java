package com.warehouse.bear.management.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.model.WarehouseRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@ToString
@Entity
@Table(name = "users_tmp",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class WarehouseAdminUser implements Serializable {
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
    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_PEC_EMAIL_REQUIRED)
    @Email(regexp = WarehouseUserConstants.WAREHOUSE_PATTERN_EMAIL)
    private String secondEmail;

    @Size(max = 120)
    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_PASSWORD_REQUIRED)
    @Pattern(regexp = WarehouseUserConstants.WAREHOUSE_PATTERN_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String temporaryPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_tmp_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<WarehouseRole> roles;

    private String dateOfBirth;
    private boolean isActive;
    private String lastLogin;
    private String createdAt;
}
