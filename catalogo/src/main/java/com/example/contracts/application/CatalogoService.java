package com.example.contracts.application;

import java.time.LocalDateTime;

import com.example.application.models.NovedadesDTO;


public interface CatalogoService {

	NovedadesDTO novedades(LocalDateTime fecha);

}