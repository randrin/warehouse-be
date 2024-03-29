package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "verifyIdentity")
public class WarehouseVerifyIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @OneToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name = "user", referencedColumnName = "userId")
    private WarehouseUser user;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String verifyType;

    @Column(nullable = true, unique = true)
    private String link;

    @Column(nullable = true, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiryDate;
}
