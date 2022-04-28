package com.warehouse.bear.management.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDimension {

    private int length;
    private int width;
    private int depth;
}
