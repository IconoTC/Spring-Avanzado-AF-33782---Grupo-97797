package com.example.contracts.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.example.domain.entities.Language;

public interface LanguagesRepository extends ListCrudRepository<Language, Integer> {
	List<Language> findAllByOrderByName();
	List<Language> findByLastUpdateGreaterThanEqualOrderByLastUpdate(LocalDateTime fecha);
}
