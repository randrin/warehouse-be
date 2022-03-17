package com.warehouse.bear.management.payload.response;

public class WarehouseMessageResponse {
	private String message;

	public WarehouseMessageResponse(String message) {
	    this.message = message;
	  }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
