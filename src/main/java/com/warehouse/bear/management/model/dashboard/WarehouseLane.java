package com.warehouse.bear.management.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseLane {

    private String name;
    private List<WarehouseRow> rows;
}
