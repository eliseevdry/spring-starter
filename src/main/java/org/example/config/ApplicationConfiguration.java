package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.web.eliseev.config.WebConfiguration;

@Import(WebConfiguration.class)
@Configuration(proxyBeanMethods = true)
public class ApplicationConfiguration {

    @Bean
    public String driver(@Value("${db.driver}") String driverName) {
        return driverName;
    }
}
