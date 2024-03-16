package org.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(2)
public class SecondAspect {
    @Around("org.example.aop.FirstAspect.anyFindByIdServiceMethod() && args(id) && target(service)")
    public Object aroundLogging(ProceedingJoinPoint proceedingJoinPoint, Object id, Object service) throws Throwable {
        log.info("AROUND before - invoked findById method in class {}, with id {}", service, id);
        try {
            Object result = proceedingJoinPoint.proceed();
            log.info("AROUND after returning - invoked findById method with id {}, return {}", id, result);
            return result;
        } catch (Throwable ex) {
            log.info("AROUND after throwing - invoked findById method with id {}, return exception {}", id, ex.getMessage());
            throw ex;
        } finally {
            log.info("AROUND after (finally) - invoked findById method with id {}", id);
        }
    }
}
