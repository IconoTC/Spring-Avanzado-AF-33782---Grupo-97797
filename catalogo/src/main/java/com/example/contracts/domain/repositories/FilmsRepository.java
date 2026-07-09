package com.example.contracts.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.example.core.contracts.domain.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.domain.entities.Film;

public interface FilmsRepository extends ProjectionsAndSpecificationJpaRepository<Film, Integer> {
	List<Film> findByLastUpdateGreaterThanEqualOrderByLastUpdate(LocalDateTime fecha);
}
