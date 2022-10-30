package com.warehouse.bear.management.enums;

public enum WarehouseStatusEnum {
    PENDING("pending"),
    ACTIVE("active"),
    DISCONNECTED("disconneted"),
    NEVER_CONNECTED("never_connected"),
    DISABLED("disabled"),
    DELETED("deleted");

    private String status;

    WarehouseStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
