package com.example.contracts.domain.repositories;

import com.example.domain.entities.Category;

public interface CategoriesRepositoryOverrides {
	Category save(Category entity);
	Category insert(Category entity);
	Category update(Category entity);
	void deleteById(Integer id);
}
