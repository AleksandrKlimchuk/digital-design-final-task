package org.example.service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({"org.example.mapper", "org.example.store.repository"})
@EnableJpaRepositories("org.example.store.repository")
@EntityScan("org.example.store.model")
public class RepositoryConfiguration {

}
