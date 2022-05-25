package com.warehouse.bear.management.repository;

import com.warehouse.bear.management.model.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface WarehouseUserRepository extends JpaRepository<WarehouseUser, Long> {

    Optional<WarehouseUser> findByUsername(String username);
    Optional<WarehouseUser> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<WarehouseUser> findByUserId(String userId);

    @Query("select u from WarehouseUser u where u.passwordReminder <= :date")
    List<WarehouseUser> getAllWarehouseUserPasswordReminder(LocalDate date);
}
