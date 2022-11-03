package com.warehouse.bear.management.model.glossary;

import com.warehouse.bear.management.model.WarehouseUser;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "glossaries")
public class WarehouseGlossary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String description;
    private String object;
    private String language;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private WarehouseUser user;

    private String createdAt;
    private String updatedAt;
}
