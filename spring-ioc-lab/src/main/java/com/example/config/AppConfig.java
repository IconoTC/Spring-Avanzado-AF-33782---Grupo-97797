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

// modificado en el paso 9
//@Configuration
//public class AppConfig {
//
//    @Bean
//    String saludoMessage(@Value("${saludo.message:Hola desde Spring IoC con Spring Boot!}") String message) {
//        return message;
//    }
//    
//    @Bean
//    Saludo saludoPersonalizado() {
//        return new Saludo("Hola desde un Bean configurado manualmente!");
//    }
//}
