package com.example.model;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("profileSaludo")
@Profile("dev")
public class SaludoDev implements Saludar {
    public String obtenerMensaje() { return "Hola desarrollador!"; }
}