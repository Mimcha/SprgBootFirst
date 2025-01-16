package org.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnProperty(name = "netology.profile.dev", havingValue = "true")
    public SystemProfile devProfile() {
        return new DevProfile();
    }

    @Bean
    @ConditionalOnProperty(name = "netology.profile.dev", havingValue = "false", matchIfMissing = true)
    public SystemProfile prodProfile() {
        return new ProductionProfile();
    }
}
