package com.warehouse.bear.management.enums;

public enum WarehouseStatusEnum {
    PENDING("pending"),
    ACTIVE("active"),
    NOT_ACTIVE("not active");

    private String status;

    WarehouseStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
