package org.example.store.model.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("org.example.store.model")
public class ModelConfig {
}
