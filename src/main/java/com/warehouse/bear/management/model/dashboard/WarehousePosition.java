package com.warehouse.bear.management.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehousePosition {

    private String position;
    private WarehouseDimension dimensions;
}
