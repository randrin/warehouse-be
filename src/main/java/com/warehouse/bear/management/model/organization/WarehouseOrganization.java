package com.warehouse.bear.management.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.bear.management.enums.WarehousePackageEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "organizations")
public class WarehouseOrganization implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String organizationId;
    private String organizationName;
    private String organizationDescription;
    private String website;

    @OneToMany
    @JoinTable(name = "organization_collaborators",
            joinColumns = @JoinColumn(name = "organizationId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<WarehouseOrganizationCollaborator> collaborators;

    private String referent;
    private String maxNumberCollaborators;
    private WarehousePackageEnum organizationPackage;
    private boolean trial;
    private String startPackage;
    private String endPackage;
    private String createdAt;
    private String updatedAt;

}
