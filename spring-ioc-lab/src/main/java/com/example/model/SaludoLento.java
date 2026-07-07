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
