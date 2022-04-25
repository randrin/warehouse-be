package com.warehouse.bear.management.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehousePosition {

    private String position;
    private WarehouseDimension dimensions;
}
