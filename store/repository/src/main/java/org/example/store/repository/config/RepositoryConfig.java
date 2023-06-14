package org.example.store.repository.config;

import org.example.store.model.config.ModelConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("org.example.store.repository")
@EnableJpaRepositories("org.example.store.repository")
@Import(ModelConfig.class)
public class RepositoryConfig {
}
