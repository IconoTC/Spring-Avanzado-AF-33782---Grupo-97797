package com.example.contracts.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.core.contracts.domain.services.DomainService;
import com.example.domain.entities.Language;

public interface LanguagesService extends DomainService<Language, Integer> {
	List<Language> novedades(LocalDateTime fecha);
}
