package com.example.model;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component("informalSaludo")
class SaludoInformal implements Saludar {
    public String obtenerMensaje() { return "¡Hola, usuario!"; }
}