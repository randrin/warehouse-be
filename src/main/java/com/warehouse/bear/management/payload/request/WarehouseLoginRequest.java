package com.warehouse.bear.management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseLoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
