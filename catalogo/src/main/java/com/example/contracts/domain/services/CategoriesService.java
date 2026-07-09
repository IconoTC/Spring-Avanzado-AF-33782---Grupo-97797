package com.example.contracts.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.core.contracts.domain.services.DomainService;
import com.example.domain.entities.Category;

public interface CategoriesService extends DomainService<Category, Integer> {
	List<Category> novedades(LocalDateTime fecha);
}
