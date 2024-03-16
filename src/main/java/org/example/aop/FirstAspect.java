package org.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Slf4j
@Order(1)
public class FirstAspect {

    /*
        @annotation - check annotation on method level
     */
    @Pointcut("org.example.aop.CommonPointcuts.isControllerLayer() && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void hasGetMapping() {
    }

    /*
        args - check method param type
        * - any param type
        .. - 0+ any params type
     */
    @Pointcut("org.example.aop.CommonPointcuts.isControllerLayer() && args(org.springframework.ui.Model,..)")
    public void hasModelParam() {
    }

    /*
        @args - check annotation on the param type
        * - any param type
        .. - 0+ any params type
     */
    @Pointcut("org.example.aop.CommonPointcuts.isControllerLayer() && @args(org.example.validation.annotation.UserInfo,..)")
    public void hasUserInfoParamAnnotation() {
    }

    /*
        bean - check bean name
     */
    @Pointcut("bean(*Service)")
    public void isServiceLayerBean() {
    }

    /*
        execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
     */
    @Pointcut("execution(public * org.example.service.*Service.findById(*))")
    public void anyFindByIdServiceMethod() {
    }

    @Before(value = "anyFindByIdServiceMethod() " +
            "&& args(id) " +
            "&& @within(transactional)",
            argNames = "id,transactional")
//    @Before("execution(public * org.example.service.*Service.findById(*))")
    public void addLogging(JoinPoint joinPoint, Object id, Transactional transactional) {
        log.info("before - invoked findById method in class {}, with id {}", joinPoint.getTarget(), id);
    }

    @AfterReturning(value = "anyFindByIdServiceMethod() && args(id)", returning = "result")
    public void loggingAfterReturning(Object result, Object id) {
        log.info("after returning - invoked findById method with id {}, return {}", id, result);
    }

    @AfterThrowing(value = "anyFindByIdServiceMethod() && args(id)", throwing = "ex")
    public void loggingAfterThrowing(Throwable ex, Object id) {
        log.info("after throwing - invoked findById method with id {}, return exception {}", id, ex.getMessage());
    }

    @After("anyFindByIdServiceMethod() && args(id)")
    public void loggingAfter(Object id) {
        log.info("after (finally) - invoked findById method with id {}", id);
    }
}
