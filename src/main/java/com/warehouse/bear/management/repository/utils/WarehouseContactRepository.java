package com.warehouse.bear.management.repository.utils;

import com.warehouse.bear.management.model.utils.WarehouseContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseContactRepository extends JpaRepository<WarehouseContact, Long> {

    Optional<WarehouseContact> findByUserId(String userId);
    
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPhonePrefix(String phonePrefix);
    boolean existsByLandlineNumber(String landlineNumber);
    boolean existsByLandlinePrefix(String landlinePrefix);
}
