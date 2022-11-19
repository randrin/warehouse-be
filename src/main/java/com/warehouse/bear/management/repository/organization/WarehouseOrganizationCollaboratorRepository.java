package com.warehouse.bear.management.repository.organization;

import com.warehouse.bear.management.model.organization.WarehouseOrganizationCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseOrganizationCollaboratorRepository extends JpaRepository<WarehouseOrganizationCollaborator, Long> {
}
