package com.warehouse.bear.management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResponseDataImageUser {

    private String fileName;
    private String downloadURL;
    private String fileType;
    private long fileSize;

}
