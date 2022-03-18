package com.warehouse.bear.management.repository;


import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.model.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRefreshTokenRepository extends JpaRepository<WarehouseRefreshToken, Long> {
  Optional<WarehouseRefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(WarehouseUser user);
}
