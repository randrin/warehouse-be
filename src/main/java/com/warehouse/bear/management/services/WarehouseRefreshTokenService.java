package com.warehouse.bear.management.services;


import com.warehouse.bear.management.exception.TokenRefreshException;
import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.repository.WarehouseRefreshTokenRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseRefreshTokenService {
  @Value("120000")
  private Long refreshTokenDurationMs;

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
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public WarehouseRefreshToken verifyExpiration(WarehouseRefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}
