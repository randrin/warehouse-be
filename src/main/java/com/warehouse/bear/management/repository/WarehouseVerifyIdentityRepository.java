package com.warehouse.bear.management.repository;

import com.warehouse.bear.management.model.WarehouseVerifyIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseVerifyIdentityRepository extends JpaRepository<WarehouseVerifyIdentity, Long> {

    Optional<WarehouseVerifyIdentity> findByLink(String link);

    Optional<WarehouseVerifyIdentity> findByCodeAndVerifyType(String code, String verifyType);
    Optional<WarehouseVerifyIdentity> findByLinkAndVerifyType(String link, String verifyType);
    Optional<WarehouseVerifyIdentity> findByLinkAndVerifyTypeAndUserId(String link, String verifyType, String userId);
}
