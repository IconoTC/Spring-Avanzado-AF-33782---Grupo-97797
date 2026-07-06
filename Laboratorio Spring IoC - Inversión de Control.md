# Laboratorio Spring IoC: Inversión de Control

## Objetivo del laboratorio

Aprender cómo funciona Spring IoC y sus principales características dentro de un proyecto Spring Boot:

- Inyección de dependencias (DI)
- Tipos de inyección: por constructor, setter y campo
- Alcances de beans (Scopes)
- Ciclo de vida (@PostConstruct, @PreDestroy)
- Uso de @Qualifier, @Primary, @Lazy, y @Profile
- Configuración por Java (@Configuration, @Bean)
- Reemplazo de configuración XML por anotaciones

### Requisitos previos

- Java 17+
- Maven o Gradle
- IDE (IntelliJ, Eclipse, VS Code con soporte para Spring Boot)

## Paso 1. Crear el Proyecto Spring Boot

### Usando Spring Initializr

1. Abre [start.spring.io](https://start.spring.io/).
2. *Alternativamente, puedes usar el Spring Starter Project de las Spring Tools, en cuyo caso debes configurar:*
    - Name: `spring-ioc-lab`
    - Description: `Laboratorio Spring IoC: Inversión de Control`
3. Configura:
    - Project: Maven Project
    - Language: Java
    - Spring Boot: 4.x.x o superior (sin SNAPSHOT)
    - Group: `com.example`
    - Artifact: `spring-ioc-lab`
    - Package name: `com.example`
4. Añade las dependencias:
    - Spring Boot DevTools (spring-boot-devtools)
5. Descarga, descomprime e importa el proyecto.

### Crear paquetes

- com.example.config
- com.example.model
- com.example.service

### Usar YAML en las propiedades

1. Sobre `src/main/resources/application.properties`, clic con botón derecho, opción **Spring**, clic en **Convert .properties to .yaml**.
2. Borra el anterior `src/main/resources/application.properties`

### Estructura base

``` bash
    spring-ioc-lab/
    ├─ src/main/java/com/example/
    │   ├─ SpringIocLabApplication.java
    │   ├─ config/
    │   ├─ model/
    │   └─ service/
    └─ src/main/resources/
        └─ application.yml
```

> [!NOTE]
> A partir de aquí, varios pasos sustituyen la misma clase con variantes diferentes. Eso es intencional: cada bloque muestra un concepto aislado y no hace falta mezclar todas las versiones a la vez.

## Paso 2: Clase principal

Esta clase arranca la aplicación y, gracias a `CommandLineRunner`, ejecuta código justo después de que Spring haya creado y cableado todos los beans. Es una forma muy cómoda de ver el comportamiento del contenedor sin montar todavía controladores web ni pruebas externas.

`SpringIocLabApplication.java`

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication  // incluye @Configuration, @EnableAutoConfiguration, @ComponentScan
public class SpringIocLabApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringIocLabApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");

    }
}
```

## Paso 3: Crear un Bean simple

`@Component` le dice a Spring que gestione esta clase como un bean del contenedor. Con el escaneo de componentes activo, no hace falta registrarla a mano en ninguna configuración adicional.

`model/Saludo.java`

```java
package com.example.model;

import org.springframework.stereotype.Component;

@Component
public class Saludo {
    public String obtenerMensaje() {
        return "Hola desde Spring IoC con Spring Boot!";
    }
}
```

## Paso 4: Inyección de dependencias (DI)

### Inyección por constructor

La inyección por constructor es la opción que deberías preferir en la mayoría de los casos. Deja la dependencia obligatoria desde el principio, facilita las pruebas y hace más claro qué necesita realmente el servicio para funcionar.

`service/PersonaService.java`

```java
package com.example.service;

import com.example.model.Saludo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    private final Saludo saludo;

    // Inyección por constructor (recomendada)
    @Autowired
    public PersonaService(Saludo saludo) {
        this.saludo = saludo;
    }

    public void decirHola() {
        System.out.println(saludo.obtenerMensaje());
    }
}
```

### Inyección por setter

La inyección por setter sigue siendo útil cuando la dependencia es opcional o cuando quieres permitir que el valor cambie más adelante. En código de aplicación, suele reservarse para casos concretos.

`service/SetterService.java`

```java
package com.example.service;

import com.example.model.Saludo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetterService {

    private Saludo saludo;

    @Autowired
    public void setSaludo(Saludo saludo) {
        this.saludo = saludo;
    }

    public void saludar() {
        System.out.println("SetterService - saludo: " + saludo.obtenerMensaje());
    }
}
```

### Inyección por campo (atributo)

La inyección por campo (atributo) es la más breve, pero también la menos recomendable para código de aplicación. Oculta las dependencias reales de la clase y complica las pruebas unitarias.

`service/FieldInjectionService.java`

```java
package com.example.service;

import com.example.model.Saludo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldInjectionService {

    @Autowired
    private Saludo saludo;

    public void saludar() {
        System.out.println("FieldInjectionService - saludo: " + saludo.obtenerMensaje());
    }
}
```

## Paso 5: Usar los bean en el arranque

En este paso pedimos al contenedor que resuelva tres servicios distintos y los use durante el arranque. Es una buena comprobación de que las dependencias ya están siendo creadas e inyectadas por Spring y no por código manual.

En la clase principal:

`SpringIocLabApplication.java`

```java
package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.service.FieldInjectionService;
import com.example.service.PersonaService;
import com.example.service.SetterService;

@SpringBootApplication
public class SpringIocLabApplication implements CommandLineRunner {

    private final PersonaService personaService;
    private final SetterService setterService;
    private final FieldInjectionService fieldService;

    public SpringIocLabApplication(PersonaService personaService,
                                   SetterService setterService,
                                   FieldInjectionService fieldService) {
        this.personaService = personaService;
        this.setterService = setterService;
        this.fieldService = fieldService;
    }

    @Override
    public void run(String... args) {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");

        System.out.println("\n1) Inyección por constructor (PersonaService):");
        personaService.decirHola();

        System.out.println("\n2) Inyección por setter (SetterService):");
        setterService.saludar();

        System.out.println("\n3) Inyección por campo (FieldInjectionService):");
        fieldService.saludar();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringIocLabApplication.class, args);
    }
}
```

El resultado al ejecutar es:

``` bash
--- Laboratorio Spring IoC: Inversión de Control ---

1) Inyección por constructor (PersonaService):
Hola desde Spring IoC con Spring Boot!

2) Inyección por setter (SetterService):
SetterService - saludo: Hola desde Spring IoC con Spring Boot!

3) Inyección por campo (FieldInjectionService):
FieldInjectionService - saludo: Hola desde Spring IoC con Spring Boot!
```

## Paso 6: Configuración Java con @Configuration y @Bean

Como paso previos, vamos a crear un constructor que permita personalizar la clase:

`model/Saludo.java`

```java
package com.example.model;

import org.springframework.stereotype.Component;

@Component
public class Saludo {
    private String message;
    
    public Saludo(String saludoMessage) {
        this.message = saludoMessage;
        System.out.println("Nuevo bean Saludo creado, soy " + this);
    }
    
    public String obtenerMensaje() {
        return message;
    }
}
```

`@Configuration` agrupa los métodos que producen beans. El nombre del método (`saludoPersonalizado`) pasa a ser el nombre del bean, así que más adelante podemos pedirlo por ese nombre o dejar que Spring lo resuelva cuando el parámetro del constructor coincida con él.

Crear la clase de configuración donde declarar los bean:

`config/AppConfig.java`

```java
package com.example.config;

import com.example.model.Saludo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    String saludoMessage(@Value("${saludo.message:Hola desde Spring IoC con Spring Boot!}") String message) {
        return message;
    }
    
    @Bean
    Saludo saludoPersonalizado() {
        return new Saludo("Hola desde un Bean configurado manualmente!");
    }
}
```

Aquí Spring decide qué instancia entregar a partir del nombre del parámetro del constructor. Es una técnica válida, aunque cuando haya varios candidatos suele ser más explícito añadir `@Qualifier`.

Para usar este bean, se puede inyectar por nombre:

`service/PersonaService.java`

```java
package com.example.service;

import com.example.model.Saludo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    private final Saludo saludo;

    // Inyección por constructor (recomendada)
    @Autowired
    public PersonaService(Saludo saludoPersonalizado) {
        this.saludo = saludoPersonalizado;
    }

    public void decirHola() {
        System.out.println(saludo.obtenerMensaje());
    }
}
```

El resultado al ejecutar es:

```java
1) Inyección por constructor (PersonaService):
Hola desde un Bean configurado manualmente!

2) Inyección por setter (SetterService):
SetterService - saludo: Hola desde Spring IoC con Spring Boot!

3) Inyección por campo (FieldInjectionService):
FieldInjectionService - saludo: Hola desde Spring IoC con Spring Boot!
```

## Paso 7: Ciclo de vida de los beans

`@PostConstruct` se ejecuta cuando Spring termina de crear e inyectar el bean. `@PreDestroy` se llama al apagar el contexto, pero recuerda que ese cierre automático está garantizado para beans singleton administrados por Spring.

Para poder intervenir en el ciclo para controlar la creación y destrucción de las instancias se puede crear los siguientes métodos:

`model/Saludo.java`

```java
package com.example.model;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class Saludo {
    private String message;
    
    public Saludo(String saludoMessage) {
        this.message = saludoMessage;
    }
    
    @PostConstruct
    public void init() {
        System.err.println("Inicializando Saludo %s...".formatted(this));
    }

    @PreDestroy
    public void destroy() {
        System.err.println("\nDestruyendo Saludo %s...".formatted(this));
    }

    public String obtenerMensaje() {
        return message;
    }
}
```

El resultado al ejecutar es:

``` bash
Inicializando Saludo com.example.model.Saludo@3af57140...
Inicializando Saludo com.example.model.Saludo@5f3d3d1...
:
Destruyendo Saludo com.example.model.Saludo@5f3d3d1...

Destruyendo Saludo com.example.model.Saludo@3af57140...
```

## Paso 8: @Scope y @Lazy

### Sin estado

Con `prototype`, cada petición al contenedor devuelve una instancia nueva. Eso significa estado independiente y también una diferencia importante en el ciclo de vida: Spring crea el bean, pero no lo destruye por ti al cerrar el contexto.

Por defecto, los beans son *singleton*. Si añadimos el ámbito prototype y modificamos el constructor (el resto de la clase no se modifica):

`model/Saludo.java`

Añadir import:

```java
import org.springframework.context.annotation.Scope;
```

Modificar:

```java
@Component
@Scope("prototype")
public class Saludo {
    private String message;
    
    public Saludo(String saludoMessage) {
        this.message = saludoMessage;
        System.out.println("Nuevo bean Saludo creado, soy " + this);
    }
    :
```

El resultado al ejecutar es:

``` bash
:
Nuevo bean Saludo creado, soy com.example.model.Saludo@1d377604
Inicializando Saludo com.example.model.Saludo@1d377604...
Nuevo bean Saludo creado, soy com.example.model.Saludo@69c14bc0
Inicializando Saludo com.example.model.Saludo@69c14bc0...
Nuevo bean Saludo creado, soy com.example.model.Saludo@39faf49
Inicializando Saludo com.example.model.Saludo@39faf49...
:
```

El valor que muestra después de la @ representa a la referencia (hashcode), su valor concreto no es lo importante (lo puede cambiar el Garbage Collector), que coincidan o no si es relevante, dado que valores distintos representan instancias distintas. Cada vez que lo inyectes, Spring creará una nueva instancia.

Si una clase singleton consume un bean prototype, Spring crea la instancia cuando se pide, pero no comparte el mismo objeto entre llamadas posteriores.

### Con estado

Los beans *singleton* comparten su estado mientras que los *prototype* no. Añadimos un contador que nos permita controlar los cambios de estado.

`model/ContadorBean.java`

```java
package com.example.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Scope("prototype")
public class ContadorBean {

    private static int instances = 0;
    private final int id;
    private int counter = 0;

    public ContadorBean() {
        id = ++instances;
    }

    @PostConstruct
    public void init() {
        System.out.println("ContadorBean init, id=" + id);
    }

    public int getNext() { 
        return ++counter; 
    }
    
    @Override
    public String toString() {
        return "ContadorBean#" + id + " counter: " + counter;
    }
}
```

En la clase principal:

`SpringIocLabApplication.java`

```java
package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.model.ContadorBean;
import com.example.service.FieldInjectionService;
import com.example.service.PersonaService;
import com.example.service.SetterService;

@SpringBootApplication
public class SpringIocLabApplication implements CommandLineRunner {

    private final PersonaService personaService;
    private final SetterService setterService;
    private final FieldInjectionService fieldService;
    private final ApplicationContext ctx;

    public SpringIocLabApplication(PersonaService personaService,
                                   SetterService setterService,
                                   FieldInjectionService fieldService,
                                   ApplicationContext ctx) {
        this.personaService = personaService;
        this.setterService = setterService;
        this.fieldService = fieldService;
        this.ctx = ctx;
    }

    @Override
    public void run(String... args) {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");

        System.out.println("\n1) Inyección por constructor (PersonaService):");
        personaService.decirHola();

        System.out.println("\n2) Inyección por setter (SetterService):");
        setterService.saludar();

        System.out.println("\n3) Inyección por campo (FieldInjectionService):");
        fieldService.saludar();
        
        var c1 = ctx.getBean(ContadorBean.class);
        var c2 = ctx.getBean(ContadorBean.class);
              System.out.println("\n4) Bean con scope %s:".formatted(c1 == c2 ? "singleton" : "prototype"));
        System.out.println("c1 = %d < %s".formatted(c1.getNext(), c1));
        System.out.println("c2 = %d < %s".formatted(c2.getNext(), c2));
        System.out.println("c1 = %d < %s".formatted(c1.getNext(), c1));
        System.out.println("c2 = %d < %s".formatted(c2.getNext(), c2));
        System.out.println("c1 = %d < %s".formatted(c1.getNext(), c1));
   }

    public static void main(String[] args) {
        SpringApplication.run(SpringIocLabApplication.class, args);
    }
}
```

Como cada instancia tiene su propio contador, el resultado al ejecutar es:

```bash
:
1) Bean con scope prototype:
c1 = 1 < ContadorBean#1 counter: 1
c2 = 1 < ContadorBean#2 counter: 1
c1 = 2 < ContadorBean#1 counter: 2
c2 = 2 < ContadorBean#2 counter: 2
c1 = 3 < ContadorBean#1 counter: 3
:
```

Y si comentamos el scope (por defecto es *singleton*):

`model/ContadorBean.java`

```java
@Component
//@Scope("prototype")
public class ContadorBean {
```

Como ahora todas las instancia comparten el contador, el resultado al ejecutar es:

```bash
:
1) Bean con scope prototype:
c1 = 1 < ContadorBean#1 counter: 1
c2 = 1 < ContadorBean#2 counter: 1
c1 = 2 < ContadorBean#1 counter: 2
c2 = 2 < ContadorBean#2 counter: 2
c1 = 3 < ContadorBean#1 counter: 3
:
```

Dejemos el scope a `prototype`:

```java
@Component
@Scope("prototype")
public class ContadorBean {
```

### @Lazy

`@Lazy` retrasa la creación hasta el primer uso real. Es útil cuando el bean tarda mucho en inicializarse o cuando, siendo costoso, no se va a necesitar siempre en la aplicación.

Con @Lazy, el bean *singleton* se crea solo cuando se inyecta la primera vez:

`model/SaludoLento.java`

```java
package com.example.model;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component("saludoLento")
@Lazy
public class SaludoLento {
    private final long init;
    public SaludoLento() {
        init = System.nanoTime();
        System.out.println("SaludoLento constructor called");
    }

    @PostConstruct
    public void init() throws InterruptedException {
        System.out.println("SaludoLento @PostConstruct - inicializando");
        Thread.sleep(5000);
        System.out.println("SaludoLento @PostConstruct - inicializado");
        System.out.println("SaludoLento ha tardado %f ms en construirse.".formatted((System.nanoTime() - init)/1_000_000.0));
        System.out.println("Hoooooooooolllllaaaaaa!");
    }
}
```

En la clase principal:

`SpringIocLabApplication.java`

```java
    @Override
    public void run(String... args) {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");
        // ...

        System.out.println("\n5) @Lazy Bean (no inicializado hasta uso):");
        System.out.println("Pidiendo lazyBean...");
        Object lazy = ctx.getBean("saludoLento");
        System.out.println("lazyBean obtenido: " + lazy.getClass().getSimpleName());
   }
```

El resultado al ejecutar es:

```bash
:
5) Bean @Lazy demo (no inicializado hasta uso):
Pidiendo lazyBean...
SaludoLento constructor called
SaludoLento @PostConstruct - inicializando
😴
SaludoLento @PostConstruct - inicializado
SaludoLento ha tardado 5000,714600 ms en construirse.
Hoooooooooolllllaaaaaa!
lazyBean obtenido: SaludoLento
:
```

## Paso 9: @Primary, @Qualifier y @Profile

### @Primary

`@Primary` indica que se debe dar preferencia a un bean cuando varios candidatos están calificados para conectar automáticamente una dependencia de un solo valor.

Vamos a crear varias implementaciones del mismo tipo para priorizar unas sobre otras:

`model/Saludar.java`

```java
package com.example.model;

public interface Saludar {
    String obtenerMensaje();
}
```

`model/SaludoFormal.java`

```java
package com.example.model;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component("formalSaludo")
class SaludoFormal implements Saludar {
    public String obtenerMensaje() { return "Saludos, estimado usuario."; }
}
```

`model/SaludoInformal.java`

```java
package com.example.model;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Primary
@Component("informalSaludo")
class SaludoInformal implements Saludar {
    public String obtenerMensaje() { return "¡Hola, usuario!"; }
}
```

Se inyecta la dependencia en el servicio:

`service/PersonaService.java`

```java
package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.Saludar;

@Service
public class PersonaService {

    private final Saludar saludo;

    public PersonaService(Saludar saludo) {
        this.saludo = saludo;
    }

    public void decirHola() {
        System.out.println(saludo.obtenerMensaje());
    }
}
```

En la clase principal:

`SpringIocLabApplication.java`

```java
    @Override
    public void run(String... args) {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");
        // ...

        System.out.println("\n6) Uso de @Primary y @Qualifier:");
        personaService.decirHola();
   }
```

Como `SaludoFormal` es el primario, el resultado al ejecutar es:

```bash
:
6) Uso de @Primary y @Qualifier:
Saludos, estimado usuario.
:
```

Si cambiamos el primario:

`model/SaludoFormal.java`

```java
//@Primary
@Component("formalSaludo")
class SaludoFormal implements Saludar {
```

`model/SaludoInformal.java`

```java
@Primary
@Component("informalSaludo")
class SaludoInformal implements Saludar {
```

Como ahora `SaludoInformal` es el primario, el resultado al ejecutar es:

```bash
:
6) Uso de @Primary y @Qualifier:
¡Hola, usuario!
:
```

### @Qualifier

`@Qualifier` resuelve el caso contrario: en vez de elegir la implementación por defecto, eliges explícitamente un bean concreto. Es la herramienta más clara cuando conviven varias variantes válidas del mismo tipo.

Para usar el bean cualificado:

`config/AppConfig.java`

```java
package com.example.config;

import com.example.model.Saludar;
import com.example.model.Saludo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    String saludoMessage(@Value("${saludo.message:Hola desde Spring IoC con Spring Boot!}") String message) {
        return message;
    }
    
    @Bean
    Saludo saludoPersonalizado() {
        return new Saludo("Hola desde un Bean configurado manualmente!");
    }

    @Bean
    @Qualifier("saludoGenerico")
    Saludar saludoGenerico() {
        return new Saludar() {
            @Override
            public String obtenerMensaje() {
                return "Hola mundo!";
            }
        };
    }
}
```

`service/PersonaService.java`

```java
package com.example.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.model.Saludar;

@Service
public class PersonaService {

    private final Saludar saludoDefault;
    private final Saludar saludoGenerico;

    public PersonaService(Saludar saludoDefault,  @Qualifier("saludoGenerico") Saludar otroSaludo) {
        this.saludoDefault = saludoDefault;
        this.saludoGenerico = otroSaludo;
    }

    public void decirHola() {
        System.out.println(saludoDefault.obtenerMensaje());
    }

    public void mostrarMensajeCalificado() {
        System.out.println(saludoGenerico.obtenerMensaje());
    }
}
```

En la clase principal:

`SpringIocLabApplication.java`

```java
    @Override
    public void run(String... args) {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");
        // ...

        System.out.println("\n6) Uso de @Primary y @Qualifier:");
        personaService.decirHola();
        personaService.mostrarMensajeCalificado();
   }
```

Si SaludoFormal es el @Primary, el resultado al ejecutar es:

```bash
:
6) Uso de @Primary y @Qualifier:
Saludos, estimado usuario.
Hola mundo!
:
```

Si SaludoInformal es el @Primary, el resultado al ejecutar es:

```bash
:
6) Uso de @Primary y @Qualifier:
¡Hola, usuario!
Hola mundo!
:
```

### @Profile

Un perfil es un grupo lógico con nombre de definiciones de beans que se registrarán en el contenedor solo si el perfil dado está activo, proporcionan un mecanismo en el contenedor central que permite el registro de diferentes juegos de beans para diferentes entornos.

Vamos a trabajar con dos perfiles: `dev` y `prod`. Definimos un par de clases, cada una asociada a uno de los perfiles.

`model/SaludoDev.java`

```java
package com.example.model;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("profileSaludo")
@Profile("dev")
public class SaludoDev implements Saludar {
    public String obtenerMensaje() { return "Hola desarrollador!"; }
}
```

`model/SaludoProd.java`

```java
@Component("profileSaludo")
@Profile("prod")
public class SaludoProd implements Saludar {
    public String obtenerMensaje() { return "Bienvenido al sistema en producción."; }
}
```

#### En la clase principal:

`SpringIocLabApplication.java`

Añadir importaciones:

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.model.Saludar;
```

Añadir al método run:

```java
    @Override
    public void run(String... args) {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");
        // ...

        System.out.println("\n7) Beans por perfil activo (consulta):");
        String[] profiles = ctx.getEnvironment().getActiveProfiles();
        System.out.println("Active profiles: " + java.util.Arrays.toString(profiles));
        if (ctx.containsBean("profileSaludo")) {
            var bean = (Saludar) ctx.getBean("profileSaludo");
            System.out.println("Bean 'profileSaludo' presente: " + bean.getClass().getSimpleName());
            System.out.println(bean.obtenerMensaje());
        } else {
            System.out.println("Bean 'profileSaludo' no definido para el perfil activo.");
        }
        // Cargar perfil 'prod'
        var prod = new AnnotationConfigApplicationContext();
        prod.getEnvironment().setActiveProfiles("prod");
        prod.scan("com.example.model");
        prod.refresh();
        var bean = (Saludar) prod.getBean("profileSaludo");
        System.out.println("Bean 'profileSaludo' cargado: " + bean.getClass().getSimpleName());
        System.out.println(bean.obtenerMensaje());
   }
```

La primera parte consulta el contexto principal que arranca la aplicación y utiliza la clase que este registrada en caso de existir. La segunda crea un contexto manual aparte para ver el mismo bean bajo otro perfil sin reiniciar el contexto.

El resultado al ejecutar es:

```bash
:
7) Beans por perfil activo (consulta):
Active profiles: []
Bean 'profileSaludo' no definido para el perfil activo.
Bean 'profileSaludo' cargado: SaludoProd
Bienvenido al sistema en producción.
:
```

Activar el perfil `dev` en application.yml

`src/main/resources/application.yml`

```yml
spring:
  application:
    name: spring-ioc-lab
  profiles:
    active: dev
```

El resultado al ejecutar es:

```bash
:
7) Beans por perfil activo (consulta):
Active profiles: [dev]
Bean 'profileSaludo' presente: SaludoDev
Hola desarrollador!
Bean 'profileSaludo' cargado: SaludoProd
Bienvenido al sistema en producción.
:
```

Con el perfil `dev` activado en el arranque principal, `SaludoDev` pasa a ser el bean registrado para `profileSaludo`. El contexto manual que se crea después con `prod` es independiente del principal, por eso se resuelve a la misma implementación.

Activar el perfil `prod` en application.yml

`src/main/resources/application.yml`

```yml
spring:
  application:
    name: spring-ioc-lab
  profiles:
    active: prod
```

El resultado al ejecutar es:

```bash
:
1) Beans por perfil activo (consulta):
Active profiles: [prod]
Bean 'profileSaludo' presente: SaludoProd
Bienvenido al sistema (perfil prod)
Bean 'profileSaludo' cargado: SaludoProd
Bienvenido al sistema (perfil prod)
:
```

Con el perfil `prod` activado en el arranque principal, `SaludoProd` pasa a ser el bean registrado para `profileSaludo`.

## Estructura final

```bash
    spring-ioc-lab/
    ├─ pom.xml
    ├─ src/main/java/com/example/
    │   ├─ SpringIocLabApplication.java        ← Clase principal
    │   ├─ config/AppConfig.java               ← Beans configurados manualmente
    │   ├─ model/
    │   │   ├─ ContadorBean.java               ← Bean @Scope("prototype")
    │   │   ├─ Saludo.java                     ← Bean simple
    │   │   ├─ Saludar.java                    ← Interfaz
    │   │   ├─ SaludoLento.java                ← Lazy Bean
    │   │   ├─ SaludoFormal.java               ← Bean formal
    │   │   ├─ SaludoInformal.java             ← Bean informal (primary)
    │   │   ├─ SaludoDev.java                  ← Bean con @Profile("dev")
    │   │   └─ SaludoProd.java                 ← Bean con @Profile("prod")
    │   └─ service/PersonaService.java         ← Servicio que usa DI
    └─ src/main/resources/application.yml      ← Configuración de perfil activo
```

## Resumen de conceptos

En conjunto, el laboratorio muestra la idea central de IoC: no construyes ningún objeto a mano (no hacer ningún `new`) en toda la aplicación, sino que declaras qué necesitas y dejas que Spring resuelva la creación, el cableado y, cuando corresponde, el ciclo de vida.

| Concepto | Implementación | Ejemplo |
| --- | --- | --- |
| Bean | @Component, @Bean | Saludo |
| DI | @Autowired, constructor | PersonaService |
| Scopes | @Scope("prototype") | Saludo, ContadorBean |
| Lazy loading | @Lazy | SaludoLento |
| Ciclo de vida | @PostConstruct, @PreDestroy | Saludo |
| Calificadores | @Qualifier, @Primary | SaludoFormal vs SaludoInformal |
| Perfiles | @Profile("dev") | SaludoDev, SaludoProd |

---
© JMA 2024
