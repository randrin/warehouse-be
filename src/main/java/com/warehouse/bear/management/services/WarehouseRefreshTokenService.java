package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.exception.TokenRefreshException;
import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.repository.WarehouseRefreshTokenRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseRefreshTokenService {

  @Autowired
  private WarehouseRefreshTokenRepository refreshTokenRepository;

  @Autowired
  private WarehouseUserRepository userRepository;

  public Optional<WarehouseRefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public WarehouseRefreshToken createRefreshToken(Long userId) {
    WarehouseRefreshToken refreshToken = new WarehouseRefreshToken();

    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(WarehouseUserConstants.WAREHOUSE_EXPIRATION_TOKEN));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public WarehouseRefreshToken verifyExpiration(WarehouseRefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), WarehouseUserResponse.WAREHOUSE_USER_REFRESH_TOKEN);
    }
    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}
