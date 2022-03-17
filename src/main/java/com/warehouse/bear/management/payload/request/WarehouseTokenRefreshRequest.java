package com.warehouse.bear.management.payload.request;

import javax.validation.constraints.NotBlank;

public class WarehouseTokenRefreshRequest {
  @NotBlank
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
