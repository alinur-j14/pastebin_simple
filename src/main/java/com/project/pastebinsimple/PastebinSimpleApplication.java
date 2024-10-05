package com.project.pastebinsimple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PastebinSimpleApplication {

    public static void main(String[] args) {
        System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");

        SpringApplication.run(PastebinSimpleApplication.class, args);
    }

}
