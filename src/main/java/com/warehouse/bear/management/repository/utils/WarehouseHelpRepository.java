package com.warehouse.bear.management.repository.utils;

import com.warehouse.bear.management.model.utils.WarehouseHelp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseHelpRepository extends JpaRepository<WarehouseHelp, Long> {
    Optional<WarehouseHelp> findByTitle(String title);

    boolean existsByTitle(String title);
}
