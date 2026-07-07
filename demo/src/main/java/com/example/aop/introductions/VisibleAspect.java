package com.example.aop.introductions;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class VisibleAspect {
	@DeclareParents(value="com.example.ioc..*", defaultImpl=com.example.aop.introductions.DefaultVisibleImpl.class)
	public static Visible mixin;
}
