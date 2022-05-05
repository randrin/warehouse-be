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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private WarehouseUser user;

    @Column(nullable = false)
    private String verifyType;

    @Column(nullable = false, unique = true)
    private String link;

    @Column(nullable = false)
    private LocalDateTime expiryDate;
}
