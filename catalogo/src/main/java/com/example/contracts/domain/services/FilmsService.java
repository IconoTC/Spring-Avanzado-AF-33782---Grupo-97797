package com.example.contracts.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.core.contracts.domain.services.ProjectionDomainService;
import com.example.core.contracts.domain.services.SpecificationDomainService;
import com.example.domain.entities.Film;

public interface FilmsService extends ProjectionDomainService<Film, Integer>, SpecificationDomainService<Film, Integer> {
	List<Film> novedades(LocalDateTime fecha);
	
	int actualizaPrecios(short año, double factor);
}
