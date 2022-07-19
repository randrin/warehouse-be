package com.warehouse.bear.management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDataResponse {

    private String fileName;
    private String downloadURL;
    private String fileType;
    private String imageType;
    private long fileSize;

}
