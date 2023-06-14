package org.example.application;

import org.example.api.config.ApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ApiConfig.class})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
