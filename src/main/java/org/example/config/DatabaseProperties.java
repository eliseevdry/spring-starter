package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties("db")
public record DatabaseProperties(String username,
                                 String password,
                                 String url,
                                 List<String> hosts,
                                 PoolProps pool,
                                 List<PoolProps> pools) {


    public record PoolProps(Integer size,
                            Integer timeout) {

    }
}
