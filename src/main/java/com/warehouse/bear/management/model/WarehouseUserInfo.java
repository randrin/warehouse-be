package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "users_infos")
public class WarehouseUserInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WarehouseUser user;

    private boolean isTemporalPassword;
    private boolean isAdminUser;
    private String status;
    private boolean isEmailPecVerified;
}
