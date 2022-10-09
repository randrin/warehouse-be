package com.warehouse.bear.management.model.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "helps")
public class WarehouseHelp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String title;
    private String description;
    private String content;
    private WarehouseStatusEnum status;
    private String createdAt;
    private String updatedAt;
}
