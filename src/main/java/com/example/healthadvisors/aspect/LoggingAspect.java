package com.example.healthadvisors.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void springBeanPointcut() {

    }

    @Before("springBeanPointcut()")
    public void beforeEndpoints(JoinPoint joinPoint) {
        log.info("Request object: " + joinPoint.toString());
    }

    @After("springBeanPointcut()")
    public void afterEndpoints(JoinPoint joinPoint) {

    }

    @AfterThrowing("springBeanPointcut()")
    public void afterThrowing(JoinPoint joinPoint){
        log.error("Threw " + joinPoint.getThis());
    }


}
