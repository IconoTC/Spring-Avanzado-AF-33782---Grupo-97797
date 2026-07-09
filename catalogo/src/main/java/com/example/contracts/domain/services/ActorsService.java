package com.example.contracts.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.core.contracts.domain.services.ProjectionDomainService;
import com.example.domain.entities.Actor;

public interface ActorsService extends ProjectionDomainService<Actor, Integer> {
	List<Actor> novedades(LocalDateTime fecha);
}
