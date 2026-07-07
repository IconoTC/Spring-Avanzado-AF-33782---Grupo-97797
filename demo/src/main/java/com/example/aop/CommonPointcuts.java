package com.example.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
//@Aspect
public class CommonPointcuts {
	@Pointcut("execution(public * com.example..*.*(..))")
	public void cualquierMetodoPublico() {}

	@Pointcut("this(com.example.ioc.contratos.Repositorio)")
	public void repositorios() {}

	@Pointcut("this(com.example.ioc.contratos.Servicio)")
	public void servicios() {}

	@Pointcut("execution(* *.add(..))")
	public void metodosAdd() {}

	@Pointcut("execution(* *.save(..))")
	public void metodosSave() {}

	@Pointcut("execution(* com.example..*.delete*(..))")
	public void metodosDelete() {}

	@Pointcut("(metodosAdd() || metodosSave() || metodosDelete()) && (repositorios() || servicios())")
	public void requiredAuthentication() {}

}
