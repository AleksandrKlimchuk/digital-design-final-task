package org.example.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.example.service", "org.example.authentication"})
public class ImportConfig {
}
