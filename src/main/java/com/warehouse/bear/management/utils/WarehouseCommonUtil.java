package com.warehouse.bear.management.utils;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WarehouseCommonUtil {

    public static String generateCurrentDateUtil() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));
        return dtf.format(LocalDateTime.now());
    }

    public static String generateUserId() {
        // Format: XXYYYYY
        return RandomStringUtils.random(2, WarehouseUserConstants.WAREHOUSE_RANDOM_LETTERS).toUpperCase()
                + RandomStringUtils.random(5, WarehouseUserConstants.WAREHOUSE_RANDOM_NUMBERS);
    }
}
