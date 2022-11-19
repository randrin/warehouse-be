package com.warehouse.bear.management.repository.organization;

import com.warehouse.bear.management.model.organization.WarehouseOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseOrganizationRepository extends JpaRepository<WarehouseOrganization, Long> {
    boolean existsByOrganizationName(String organizationName);

    WarehouseOrganization findByOrganizationId(String organizationId);
}
