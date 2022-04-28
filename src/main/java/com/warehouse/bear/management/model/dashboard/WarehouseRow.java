package com.warehouse.bear.management.model.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRow {

    private int row;
    private List<WarehouseShelf> shelves;
}
