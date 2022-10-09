package com.warehouse.bear.management.model.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.bear.management.constants.WarehouseUserConstants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "address")
public class WarehouseAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_COUNTRY_REQUIRED)
    private String country;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_STATE_REQUIRED)
    private String state;

    @NotBlank(message = WarehouseUserConstants.WAREHOUSE_ADDRESS_REQUIRED)
    private String addressLine;

    private int zipCode;

}
