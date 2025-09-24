package com.khoahd7621.youngblack.utils;

import org.springframework.stereotype.Component;

@Component
public class SkuUtil {
    public String getProductSku(String rootSku, String colorName, String sizeName) {
        return rootSku.trim().toUpperCase() + "-"
                + colorName.substring(0, 2).toUpperCase() + "-"
                + sizeName.toUpperCase();
    }
}
