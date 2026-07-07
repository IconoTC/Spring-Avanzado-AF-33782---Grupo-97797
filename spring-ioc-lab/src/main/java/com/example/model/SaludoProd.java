package com.example.model;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("profileSaludo")
@Profile("prod")
public class SaludoProd implements Saludar {
    public String obtenerMensaje() { return "Bienvenido al sistema en producción."; }
}
