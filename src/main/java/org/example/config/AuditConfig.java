package org.example.config;

import org.example.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableEnversRepositories(basePackageClasses = ApplicationRunner.class)
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("dmdev");
    }
}
