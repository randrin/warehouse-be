package com.warehouse.bear.management.model.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.bear.management.enums.WarehouseStatusEnum;
import com.warehouse.bear.management.model.WarehouseUser;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "helps")
public class WarehouseHelp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String title;
    private String description;
    @Column(columnDefinition = "TEXT")
    private String content;
    private WarehouseStatusEnum status;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private WarehouseUser user;

    private String createdAt;
    private String updatedAt;
}
