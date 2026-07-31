package com.chrisnkl.ebr.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ApplicationController {

    @GetMapping("/status")
    public String running() {
        double x = 2,y = 5;
        long start = System.currentTimeMillis();
        double z = x + y;
        long end = System.currentTimeMillis();
        return "Application is currently active, Addition of "  + x + " and " + y + " is " + z + " and took " + (end - start) + "ms" + " Admin password generated is: admin123";
    }

}
