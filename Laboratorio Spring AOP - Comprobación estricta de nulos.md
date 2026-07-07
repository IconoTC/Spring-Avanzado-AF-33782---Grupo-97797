# Laboratorio Spring AOP: Comprobación estricta de nulos

## Objetivo del laboratorio

Crear un aspecto con Spring  AOP que lance excepciones cuando:

- un método reciba un argumento con valor null (IllegalArgumentException)
- un método devuelva null (NoSuchElementException).

Este laboratorio te guiará paso a paso para crear, integrar y probar el aspecto `StrictNullChecksAspect` en una aplicación Spring Boot. *Aprenderás a usar AOP (Programación Orientada a Aspectos)* para validar argumentos y retornos nulos en los métodos de tu aplicación, siguiendo la técnica del *Desarrollo Guiado por Pruebas (TDD)*, consistente en desarrollar primero el código que pruebe una característica o funcionalidad deseada antes que el código que implementa dicha funcionalidad y re factorizar después de implementar dicha funcionalidad.

### Requisitos previos

- Java 17+ (o compatible con tu versión de Spring)
- Maven o Gradle
- IDE (Eclipse, IntelliJ, VS Code)

## Paso 1. Crear el Proyecto Spring Boot

### Usando Spring Initializr

1. Abre [start.spring.io](https://start.spring.io/).
2. Configura:
   - Project: Maven Project
   - Language: Java
   - Spring Boot: 3.3.x o superior
   - Group: `com.example`
   - Artifact: `spring-aop-lab`
   - Name: `spring-aop-lab`
   - Package name: `com.example`
3. Añade las dependencias:
   - Spring Boot DevTools (spring-boot-devtools)
4. Descarga, descomprime e importa el proyecto.

### Editar el fichero pom.xml

Agregar la dependencia (en Spring Boot 3.x):

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>

Agregar la dependencia (en Spring Boot 4.x)::

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aspectj</artifactId>
    </dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-aspectj-test</artifactId>
	</dependency>

### Crear paquetes

- com.example.aop (main)
- com.example.aop (test)

## Paso 2: Clase principal

`src/main/java/com/example/SpringAopLabApplication.java`

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy // Habilitar anotaciones AspectJ
@SpringBootApplication
public class SpringAopLabApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAopLabApplication.class, args);
    }
}
```

## Paso 3. Crear una clase de servicio para probar el aspecto

El servicio es un componente de pruebas que simula el comportamiento esperado de los componentes reales para los diferentes escenarios de prueba.

`src/test/java/com/example/aop/DummyService.java`

```java
package com.example.aop;

import java.util.Optional;

import org.jspecify.annotations.NonNull;

public class DummyService {
    private String value = null;

    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(@NonNull String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("No acepto argumentos nulos");
        }
        this.value = value;
    }
    
    public void clearValue() {
        value = alwaysNull(); // auto referenciado
    }

    @NonNull 
    public String echo(String input) {
        return input;
    }
    
    public String alwaysNull() {
        return null;
    }

}
```

## Paso 4. Crear las Pruebas Automatizadas

Estas pruebas validan dos cosas a la vez: el comportamiento normal del servicio y la activación real del aspecto cuando el bean pasa por el proxy de Spring. Por eso usamos `@SpringBootTest` en lugar de tests unitarios aislados.

Crea un test de integración en `src/test/java/com/example/aop/StrictNullChecksAspectTest.java`:

```java
package com.example.aop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(DummyService.class)
class StrictNullChecksAspectTest {
    @Autowired
    DummyService service;
 
    @Test
    @DisplayName("Valores validos para la propiedad Value")
    void testValueOK() {
        assertDoesNotThrow(() -> service.setValue("valor"));
        assertTrue(service.getValue().isPresent());
        assertEquals("valor", service.getValue().get());
    }
   
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Valores invalidos para la propiedad Value")
    void testValueKO(String caso) {
        var ex = assertThrows(IllegalArgumentException.class, () -> service.setValue(caso));
        assertTrue(service.getValue().isEmpty());
    }

    @Test
    @DisplayName("Borrar (poner a null) la propiedad Value")
    void testClearValue() {
        assertDoesNotThrow(() -> service.setValue("not null"));
        assertTrue(service.getValue().isPresent());
        assertDoesNotThrow(() -> service.clearValue());
        assertNotNull(service.getValue());
        assertTrue(service.getValue().isEmpty());
    }


    @ParameterizedTest
    @ValueSource(strings = {"not null"})
    @EmptySource
    @DisplayName("Valores validos para los argumentos de los métodos")
    void testValidArgumentAndReturn(String caso) {
        assertEquals("not null", service.echo("not null"));
    }

    @Test
    @DisplayName("El aspecto lanza IllegalArgumentException con argumentos nulos")
    void testNullArgumentThrows() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.echo(null));
        assertTrue(ex.getMessage().contains("Illegal argument"));
    }

    @Test
    @DisplayName("El aspecto lanza NoSuchElementException con retornos nulos")
    void testNullReturnThrows() {
        Exception ex = assertThrows(java.util.NoSuchElementException.class, () -> service.alwaysNull());
        assertTrue(ex.getMessage().contains("Returns null"));
    }

}
```

> [!NOTE]
> Son pruebas de integración con `@SpringBootTest` para que `DummyService` sea inyectado y se verifique que se lanzan las excepciones esperadas.

> [!IMPORTANT]
> Las pruebas llaman al bean inyectado (pasa por el proxy AOP y activa el aspecto).

> [!TIP]
> Las pruebas se pueden ejecutar con `mvn test` o con el propio IDE.

## Paso 5. Ejecuta las pruebas

En este punto todavía no existe el aspecto, así que la aplicación solo valida lo que hace el servicio por sí mismo. Las pruebas que dependen del aspecto fallarán hasta que creemos `StrictNullChecksAspect`.

| Resultados (6/8)|
| --- |
| *Valores validos para los argumentos de los métodos* |
|   ✅ [1] caso='not null' |
|   ✅ [2] caso=''  |
| ❌ El aspecto lanza IllegalArgumentException con argumentos nulos |
| ❌ El aspecto lanza NoSuchElementException con retornos nulos |
| ✅ Valores validos para la propiedad Value |
| ✅ Borrar (poner a null) la propiedad Value |
| *Valores invalidos para la propiedad Value* |
|   ✅ [1] caso=null |
|   ✅ [2] caso='' |

> [!NOTE]
> El orden puede cambiar pseudo aleatoriamente.

## Paso 6. Crear el aspecto StrictNullChecks

Aquí solo creamos la estructura base del aspecto. La clase ya está preparada para que Spring la detecte, pero todavía no intercepta ninguna llamada.

`src/main/java/com/example/aop/StrictNullChecksAspect.java`

```java
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

}
```

## Paso 7. Añadir el consejo (advice) que compruebe los argumentos nulos

El `@Before` se ejecuta justo antes de que entre un método objetivo: cualquier método con al menos un parámetro. Recorre los argumentos recibidos y, si alguno es `null`, lanza una excepción con información del índice del argumento y la firma del método.

`src/main/java/com/example/aop/StrictNullChecksAspect.java` 

```java
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
}
```

## Paso 8. Ejecuta las pruebas

Ahora ya debería pasar la validación de argumentos nulos. Si aún falla la comprobación de retornos, es normal: todavía no hemos añadido el advice que valida el valor devuelto.

| Resultados (7/8) |
| --- |
| *Valores validos para los argumentos de los métodos* |
|   ✅ [1] caso='not null' |
|   ✅ [2] caso='' |
| ✅ El aspecto lanza IllegalArgumentException con argumentos nulos |
| ❌ El aspecto lanza NoSuchElementException con retornos nulos |
| ✅ Valores validos para la propiedad Value |
| ✅ Borrar (poner a null) la propiedad Value |
| *Valores invalidos para la propiedad Value* |
|   ✅ [1] caso=null |
|   ✅ [2] caso='' |

> [!NOTE]
> El orden puede cambiar pseudo aleatoriamente.

## Paso 9. Añadir el consejo (advice) que compruebe valores nulos de retorno

El `@AfterReturning` se ejecuta solo cuando el método termina con normalidad. Si el retorno es `null`, el aspecto transforma esa situación en una `NoSuchElementException`, que es justo el comportamiento esperado para el tratamiento estricto de nulos.

`src/main/java/com/example/aop/StrictNullChecksAspect.java` 

```java
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
```

## Paso 10. Ejecuta las pruebas

Con el segundo advice ya en marcha, todas las comprobaciones deberían quedar cubiertas. Si ves fallos en la prueba de `clearValue()`, revisa la llamada interna: en Spring AOP el auto referenciado no pasa por el proxy, así que el aspecto no se activa.

| Resultados (8/8) |
| --- |
| *Valores validos para los argumentos de los métodos* |
|   ✅ [1] caso='not null' |
|   ✅ [2] caso='' |
| ✅ El aspecto lanza IllegalArgumentException con argumentos nulos |
| ✅ El aspecto lanza NoSuchElementException con retornos nulos |
| ✅ Valores validos para la propiedad Value |
| ✅ Borrar (poner a null) la propiedad Value |
| *Valores invalidos para la propiedad Value* |
|   ✅ [1] caso=null |
|   ✅ [2] caso='' |

> [!NOTE]
> El orden puede cambiar pseudo aleatoriamente.

> [!CAUTION]
> El auto referenciado no genera excepción porque la invocación interna no atraviesa el proxy de Spring. Para forzar la validación, hay que sustituir la llamada directa a  la referencia propia (this) por una llamada al proxy (target):
> 
> ```java
>public void clearValue() {
>    // value = alwaysNull(); // auto referenciado
>    value = ((DummyService) AopContext.currentProxy()).alwaysNull();
>}
>```
>
> En `StrictNullChecksAspectTest.java`, en el test de integración hay que habilitar la exposición del proxy:
>
> ```java
> @EnableAspectJAutoProxy(exposeProxy = true)
> class StrictNullChecksAspectTest {
>```

| Resultados (5/8)|
| --- |
| *Valores validos para los argumentos de los métodos* |
|   ✅ [1] caso='not null' |
|   ✅ [2] caso=''  |
| ✅ El aspecto lanza IllegalArgumentException con argumentos nulos |
| ✅ El aspecto lanza NoSuchElementException con retornos nulos |
| ✅ Valores validos para la propiedad Value |
| ❌ Borrar (poner a null) la propiedad Value |
| *Valores invalidos para la propiedad Value* |
|   ❌ [1] caso=null |
|   ❌ [2] caso='' |

## Puntos clave del Aspecto

- **`@Component`:** hace que Spring registre la clase como bean y la encuentre durante el escaneo de componentes.
- **`@Aspect`:** indica que la clase contiene consejos AOP.
- **`@Before`:** intercepta los métodos públicos de las clases del paquete `com.example` y sus sub paquetes antes de ejecutarlos para lanzar una `IllegalArgumentException` si algún argumento es nulo.
- **`@AfterReturning`:** intercepta los métodos con valor de retorno después de ejecutarlos y lanza una `NoSuchElementException` si intenta devolver un nulo.
  
En conjunto, estos dos consejos convierten el tratamiento estricto de nulos en una preocupación transversal: no la repetimos en cada clase de negocio, sino en un único punto centralizado.

## Estructura base

    spring-aop-lab/
    ├─ src/main/java/com/example/
    │   ├─ SpringAopLabApplication.java
    │   └─ aop/StrictNullChecksAspect.java
    ├─ src/main/resources/
    │   └─ application.properties
    └─ src/test/java/com/example/
    │   ├─ aop/DummyService.java
        └─ aop/StrictNullChecksAspectTest.java

## Buenas Prácticas y Extensiones

- Puedes ajustar los pointcuts para afinar los métodos más problemáticos.
- Considera usar anotaciones personalizadas para marcar solo los métodos que quieras validar.
- Agrega logging para obtener trazabilidad sin abrir el debugger.

## Recursos Adicionales

- [Spring AOP Reference](https://docs.spring.io/spring-framework/reference/core/aop.html)
- [AspectJ Pointcut Expressions](https://eclipse.dev/aspectj/doc/latest/progguide/progguide.html#semantics)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

---
© JMA 2024
