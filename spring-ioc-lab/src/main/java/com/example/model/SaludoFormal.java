package com.example.model;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Primary
@Component("formalSaludo")
class SaludoFormal implements Saludar {
    public String obtenerMensaje() { return "Saludos, estimado usuario."; }
}
