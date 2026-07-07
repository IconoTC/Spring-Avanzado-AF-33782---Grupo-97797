package com.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuthenticationAspect {
	private AuthenticationService srv;

	public AuthenticationAspect(AuthenticationService srv) {
		this.srv = srv;
	}
	
	@Around("com.example.aop.CommonPointcuts.requiredAuthentication()")
	public Object requiereAutenticacion(ProceedingJoinPoint jp) throws Throwable {
		if(srv.isAuthenticated())
			return jp.proceed();
		throw new SecurityException("Unauthorized.", new SecurityException("Aspect for " + jp.getSignature()));	
	}

}
