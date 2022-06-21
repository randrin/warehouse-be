package com.warehouse.bear.management.repository.admin;

import com.warehouse.bear.management.model.admin.WarehouseAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseAdminUserRepository extends JpaRepository<WarehouseAdminUser, Long> {

    Optional<WarehouseAdminUser> findByUsername(String username);
    Optional<WarehouseAdminUser> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<WarehouseAdminUser> findByUserId(String userId);
}
