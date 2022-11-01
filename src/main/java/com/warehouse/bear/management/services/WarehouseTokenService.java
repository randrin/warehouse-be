package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.exception.TokenRefreshException;
import com.warehouse.bear.management.model.WarehouseRefreshToken;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseVerifyIdentity;
import com.warehouse.bear.management.repository.WarehouseRefreshTokenRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.WarehouseVerifyIdentityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseTokenService {

  private WarehouseRefreshTokenRepository refreshTokenRepository;
  private WarehouseUserRepository userRepository;
  private WarehouseVerifyIdentityRepository verifyIdentityRepository;

  public Optional<WarehouseRefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public WarehouseRefreshToken createRefreshToken(Long userId) {
    // TODO: Check if the user already existed in DB with user_id, then cancel and update with the new one
    WarehouseRefreshToken refreshToken = new WarehouseRefreshToken();

    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(WarehouseUserConstants.WAREHOUSE_EXPIRATION_TOKEN));
    refreshToken.setToken(UUID.randomUUID().toString());

    return refreshTokenRepository.save(refreshToken);
  }

  public WarehouseVerifyIdentity createForgotPassowordLink(String userId) {
    // TODO: Check if the user already existed in DB with user_id, then cancel and update with the new one
    WarehouseVerifyIdentity verifyIdentity = new WarehouseVerifyIdentity();

    verifyIdentity.setUser(userRepository.findByUserId(userId).get());
    verifyIdentity.setUserId(userId);
    verifyIdentity.setVerifyType(WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_RESET_PASSWORD);
    verifyIdentity.setExpiryDate(LocalDateTime.now().plusMinutes(WarehouseUserConstants.WAREHOUSE_EXPIRATION_LINK));
    verifyIdentity.setLink(UUID.randomUUID().toString());

    return verifyIdentityRepository.save(verifyIdentity);
  }

  public WarehouseVerifyIdentity createVerificationEmailLink(String userId) {
    // TODO: Check if the user already existed in DB with user_id, then cancel and update with the new one
    WarehouseVerifyIdentity verifyIdentity = new WarehouseVerifyIdentity();

    Optional<WarehouseUser> user = userRepository.findByUserId(userId);

    verifyIdentity.setUser(user.get());
    verifyIdentity.setUserId(userId);
    verifyIdentity.setVerifyType(WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL);
    verifyIdentity.setExpiryDate(LocalDateTime.now().plusMinutes(WarehouseUserConstants.WAREHOUSE_EXPIRATION_LINK));
    verifyIdentity.setLink(UUID.randomUUID().toString());
    verifyIdentity.setCode("");

    return verifyIdentityRepository.save(verifyIdentity);
  }

  public WarehouseVerifyIdentity createVerificationEmailPecCode(String userId, String code) {
    // TODO: Check if the user already existed in DB with user_id, then cancel and update with the new one
    WarehouseVerifyIdentity verifyIdentity = new WarehouseVerifyIdentity();

    Optional<WarehouseUser> user = userRepository.findByUserId(userId);

    verifyIdentity.setUser(user.get());
    verifyIdentity.setUserId(userId);
    verifyIdentity.setVerifyType(WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL_PEC);
    verifyIdentity.setExpiryDate(LocalDateTime.now().plusMinutes(WarehouseUserConstants.WAREHOUSE_EXPIRATION_LINK));
    verifyIdentity.setCode(code);

    return verifyIdentityRepository.save(verifyIdentity);
  }

  public WarehouseRefreshToken verifyExpiration(WarehouseRefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), WarehouseUserResponse.WAREHOUSE_USER_REFRESH_TOKEN);
    }
    return token;
  }
}
