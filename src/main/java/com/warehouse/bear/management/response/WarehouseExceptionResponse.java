package com.warehouse.bear.management.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseExceptionResponse {

    private HttpStatus status;
    private List<String> errors;
    private LocalDateTime timestamp;
    private String path;
}
