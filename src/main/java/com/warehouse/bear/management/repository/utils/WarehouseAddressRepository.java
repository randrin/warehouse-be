package com.warehouse.bear.management.repository.utils;

import com.warehouse.bear.management.model.utils.WarehouseAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseAddressRepository extends JpaRepository<WarehouseAddress, Long> {

    Optional<WarehouseAddress> findByUserId(String userId);
}
