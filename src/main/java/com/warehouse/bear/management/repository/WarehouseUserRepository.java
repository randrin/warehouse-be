package com.warehouse.bear.management.repository;

import com.warehouse.bear.management.model.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseUserRepository extends JpaRepository<WarehouseUser, Long> {

    Optional<WarehouseUser> findByUsername(String username);
    Optional<WarehouseUser> findByEmail(String email);
    Optional<WarehouseUser> findByUserId(String userId);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    boolean existsByEmailPec(String emailPec);

}
