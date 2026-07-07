package com.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
//@Aspect
public class AspectoImplAspect {
	@Pointcut("execution(public * com.example.ioc..*.*(..))")
	public void miPuntoDeCorte() {
	}
//	@Before("miPuntoDeCorte()")
//	@Order(100)
//	public void consejoAntesDelMetodo1(JoinPoint jp) {
//		System.err.println(">>> Soy primer un consejo antesDelMetodo " + jp.getSignature());
//	}
//
//	@Before("miPuntoDeCorte()")
//	@Order(20)
//	public void consejoAntesDelMetodo2(JoinPoint jp) {
//		System.err.println(">>> Soy segundo un consejo antesDelMetodo " + jp.getSignature());
//	}
//	@Order(10)
//	@Before("execution(int com.example.ioc..*.get*(..))")
//	public void impideHacerlo(JoinPoint jp) {
//		System.err.println(">>> Impidiendo la ejecucion del metodo " + jp.getSignature());
//		throw new RuntimeException("Forzando excepcion en consejo antesDelMetodo");
//	}
//	
//	@After("miPuntoDeCorte()")
//	public void consejoDespuesDelMetodo(JoinPoint jp) {
//		System.err.println(">>> Soy un consejo despuesDelMetodo " + jp.getSignature());
//	}

//	@AfterReturning(pointcut="execution(int com.example.ioc..*.get*())",	returning="retVal")
//	public void consejoDespuesDeGetPropiedad(JoinPoint jp, int retVal) {
//		System.err.println(">>> La funcion '" + jp.getSignature() + "' ha devuelto " + retVal);
//	}

//	@AfterThrowing(pointcut="execution(void com.example..*.set*(..))",	throwing = "ex")
//	public void consejoDespuesDeGetPropiedad(JoinPoint jp, Throwable ex) {
//		System.err.println(">>> El setter '" + jp.getSignature() + "' ha devuelto la excepcion " + ex.getClass().getSimpleName());
//	}

//	@Around( "execution(* com.example.ioc..*.get*())")
//	public Object consejoQueEnvuelveGetters(ProceedingJoinPoint jp) throws Throwable {
//		System.err.println(">>> Soy el previo consejoQueEnvuelveGetters " + jp.getSignature());
//		int o = (int) jp.proceed();
//		System.err.println(">>> Soy el posterior consejoQueEnvuelveGetters " + jp.getSignature());
//		return o;
//	}

//	@Around("miPuntoDeCorte()")
//	@Around( "execution(* com.example.ioc..*.get*())")
//	public Object consejoQueEnvuelveAlMetodo(ProceedingJoinPoint jp) throws Throwable {
//		System.err.println(">>> Soy el previo queEnvuelveAlMetodo " + jp.getSignature());
//		var o = jp.proceed();
//		System.err.println(">>> Soy el posterior queEnvuelveAlMetodo " + jp.getSignature());
//		if(o != null ) {
//			System.err.println(">>> Valor devuelto " + o);
//			if(o instanceof Number v) {
//				System.err.println(">>> Valor calculado " + v.doubleValue() * 2);
//				return v.doubleValue() * 2;
//			}
//		}
//		return o;
//	}
	
//	@Around("execution(void com.example.ioc..*.set*(String))")
//	public void interceptaExcepcion(ProceedingJoinPoint jp) throws Throwable {
//		System.err.println(">>> Soy el previo interceptaExcepcion " + jp.getSignature());
//		try {
//			jp.proceed();
//		} catch (Exception e) {
//			System.err.println(">>> Excepcion tratada en el aspecto " + jp.getSignature());
//		}
//	}

//	@Around("execution(void com.example.ioc..*.set*(String))")
//	public void cambiaArgumentos(ProceedingJoinPoint jp) throws Throwable {
//		System.err.println(">>> Soy el previo queEnvuelveAlMetodo " + jp.getSignature());
//		if (jp.getArgs() != null && jp.getArgs().length > 0 && jp.getArgs()[0] instanceof String v) {
//			System.err.println(">>> Cambiando argumento a mayusculas");
//			jp.proceed(new Object[] { v.toUpperCase() });
////			jp.proceed();
//		} else {
//			System.err.println(">>> Cambiando argumento vacio a 'Valor por defecto'");
//			jp.proceed(new Object[] { "Valor por defecto" });
//		}
//	}

}
