package com.example.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
@Scope("prototype")
public class Saludo {
    private String message;
    
    public Saludo(String saludoMessage) {
        this.message = saludoMessage;
        System.out.println("Nuevo bean Saludo creado, soy " + this);
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

//modificado en el paso 8
//@Component
//public class Saludo {
//    private String message;
//    
//    public Saludo(String saludoMessage) {
//        this.message = saludoMessage;
//    }
//    
//    @PostConstruct
//    public void init() {
//        System.err.println("Inicializando Saludo %s...".formatted(this));
//    }
//
//    @PreDestroy
//    public void destroy() {
//        System.err.println("\nDestruyendo Saludo %s...".formatted(this));
//    }
//
//    public String obtenerMensaje() {
//        return message;
//    }
//}

// modificado en el paso 7
//package com.example.model;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class Saludo {
//    private String message;
//    
//    public Saludo(String saludoMessage) {
//        this.message = saludoMessage;
//        System.out.println("Nuevo bean Saludo creado, soy " + this);
//    }
//    
//    public String obtenerMensaje() {
//        return message;
//        // del paso 3:
//        // return "Hola desde Spring IoC con Spring Boot!";
//    }
//}
