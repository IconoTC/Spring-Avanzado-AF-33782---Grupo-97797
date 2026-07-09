package com.example.contracts.domain.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.domain.entities.Category;

public interface CategoriesRepository extends ListCrudRepository<Category, Integer> {
	
	@Override
	@RestResource(exported = false)
	void deleteById(Integer id);

}
