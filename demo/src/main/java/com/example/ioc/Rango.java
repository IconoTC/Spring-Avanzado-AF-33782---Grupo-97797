package com.example.ioc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("rango")
public class Rango {
    private int min;
    private int max;
}

//Hay que activar @ConfigurationPropertiesScan en @SpringBootApplication
//@ConfigurationProperties(prefix = "rango")
//public record Rango(int min, int max) {}

