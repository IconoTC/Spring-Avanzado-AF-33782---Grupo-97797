package com.example.contracts.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.example.domain.entities.Category;

public interface CategoriesRepository extends ListCrudRepository<Category, Integer>, CategoriesRepositoryOverrides {
	List<Category> findAllByOrderByName();
	List<Category> findByLastUpdateGreaterThanEqualOrderByLastUpdate(LocalDateTime fecha);
}
