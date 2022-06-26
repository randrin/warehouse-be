package com.warehouse.bear.management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseLoginRequest {
	private String username;
	private String password;
}
