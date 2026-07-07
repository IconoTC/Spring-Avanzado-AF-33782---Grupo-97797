package com.example.aop;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class StrictNullChecksAspect {
    @Before("execution(public * com.example..*.*(*,..))")
    public void nullArgument(JoinPoint jp) {
        for(var i = 0; i < jp.getArgs().length; i++) {
            if(Objects.isNull(jp.getArgs()[i])) 
                throw new IllegalArgumentException(String.format("Illegal argument %d in method '%s'", i + 1, jp.getSignature()));
        }
    }
    
    @AfterReturning(pointcut="execution(* com.example..*.*(..)) && !execution(void *(..))", returning="retVal")
    public void nullReturn(JoinPoint jp, Object retVal) {
        if(Objects.isNull(retVal))
            throw new NoSuchElementException(String.format("Returns null in method '%s'", jp.getSignature()));
    }

}