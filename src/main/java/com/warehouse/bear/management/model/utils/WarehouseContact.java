package com.warehouse.bear.management.model.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "contacts")
public class WarehouseContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_PREFIX_PHONE_REQUIRED)
    private String phonePrefix;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_PHONE_REQUIRED)
    private String phoneNumber;

    private String landlinePrefix;
    private String landlineNumber;
}
