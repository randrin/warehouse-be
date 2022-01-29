package com.warehouse.bear.management.repository;

import com.warehouse.bear.management.model.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseUserRepository extends JpaRepository<WarehouseUser, Long> {

    WarehouseUser findByUsername(String username);
}
