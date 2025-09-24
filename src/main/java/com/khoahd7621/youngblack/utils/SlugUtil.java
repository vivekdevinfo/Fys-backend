package com.khoahd7621.youngblack.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SlugUtil {

    public String getSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-") + "-" + new Date().getTime();
    }

}
