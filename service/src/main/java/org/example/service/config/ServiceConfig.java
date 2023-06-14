package org.example.service.config;

import org.example.mapper.config.MapperConfig;
import org.example.store.repository.config.RepositoryConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RepositoryConfig.class, MapperConfig.class})
@ComponentScan("org.example.service")
public class ServiceConfig {

}
