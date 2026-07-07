package com.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.ioc.anotaciones.Pruebas;

//@Pruebas
@Component
//@Aspect
public class LoggerAspect {
	Logger log = LoggerFactory.getLogger(LoggerAspect.class);
	
	@Before("@annotation(com.example.aop.anotations.Logging) || @annotation(com.example.aop.anotations.LoggerAll)")
	public void registerLogging(JoinPoint jp) {
		log.warn(jp.getSignature() + " executing.");
	}
	
	@After("@annotation(com.example.aop.anotations.Logged) || @annotation(com.example.aop.anotations.LoggerAll)")
	public void registerLogged(JoinPoint jp) {
		log.warn(jp.getSignature() + " executed.");
	}
	

}
