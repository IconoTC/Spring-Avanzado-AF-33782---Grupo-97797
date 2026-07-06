package com.example.ioc.anotaciones;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Repository;

@Retention(RUNTIME)
@Target(TYPE)
@Repository
@Pruebas
public @interface MockRepository {

}
