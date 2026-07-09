package com.example.contracts.domain.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.example.domain.entities.Category;

public interface CategoriesRepository extends ListCrudRepository<Category, Integer> {

}
