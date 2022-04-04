package com.warehouse.bear.management.repository;


import com.warehouse.bear.management.enums.WarehouseRoleEnum;
import com.warehouse.bear.management.model.WarehouseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRoleRepository extends JpaRepository<WarehouseRole, Long> {
  Optional<WarehouseRole> findByName(WarehouseRoleEnum name);
}
