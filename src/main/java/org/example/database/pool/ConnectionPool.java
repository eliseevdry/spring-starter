package org.example.database.pool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
@Slf4j
@Component("pool1")
@RequiredArgsConstructor
public class ConnectionPool {
    @Value("${db.username}")
    private final String username;
    @Value("${db.pool.size}")
    private final Integer poolSize;


    @PostConstruct
    private void init() {
        log.info("I am init");
    }

    @PreDestroy
    private void destroy() {
        log.info("I am destroy");
    }
}
