package com.example.contracts.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.example.core.contracts.domain.repositories.InsertableAndUpdatableRepository;
import com.example.core.contracts.domain.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.domain.entities.Actor;

public interface ActorsRepository extends ProjectionsAndSpecificationJpaRepository<Actor, Integer>, InsertableAndUpdatableRepository<Actor, Integer> {
	List<Actor> findByLastUpdateGreaterThanEqualOrderByLastUpdate(LocalDateTime fecha);
	
}
