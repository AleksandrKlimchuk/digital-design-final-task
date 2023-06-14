package org.example.api.config;

import org.example.authentication.config.AuthenticationConfig;
import org.example.service.config.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"org.springdoc", "org.example.api"})
@Import({ServiceConfig.class, AuthenticationConfig.class})
public class ApiConfig {
}
