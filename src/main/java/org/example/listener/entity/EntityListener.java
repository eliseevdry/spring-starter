package org.example.listener.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntityListener {
    @EventListener(condition = "#p0.accessType.name() == 'READ'")
    @Order(10)
    public void acceptEntity(EntityEvent entityEvent) {
        log.info("Entity: " + entityEvent);
    }
}
