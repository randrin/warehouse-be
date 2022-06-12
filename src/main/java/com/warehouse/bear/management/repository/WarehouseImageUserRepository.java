package com.warehouse.bear.management.repository;

import com.warehouse.bear.management.model.WarehouseImageUser;
import com.warehouse.bear.management.model.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseImageUserRepository extends JpaRepository<WarehouseImageUser, Long> {
    Optional<WarehouseImageUser> findByUser(WarehouseUser user);
}


