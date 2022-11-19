package com.warehouse.bear.management.enums;

public enum WarehousePackageEnum {
    STANDARD("standard"),
    PREMIUM("premium"),
    BUSINESS("business");

    private String packageName;

    WarehousePackageEnum(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}
