package com.warehouse.bear.management.repository;

import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseUserInfoRepository extends JpaRepository<WarehouseUserInfo, Long> {

    Optional<WarehouseUserInfo> findByUser(WarehouseUser userId);
}
