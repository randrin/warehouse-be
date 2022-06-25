package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(nullable = false, unique = true)
    private String link;

    @Column(nullable = false)
    private LocalDateTime expiryDate;
}
