package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.config.condition.JpaCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@Conditional(JpaCondition.class)
public class JpaConfiguration {

    @PostConstruct
    private void init() {
        log.info("JpaCondition was enabled");
    }
}
