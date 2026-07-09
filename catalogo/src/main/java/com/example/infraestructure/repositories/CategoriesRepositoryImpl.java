package com.example.infraestructure.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.CategoriesRepositoryOverrides;
import com.example.core.contracts.domain.exceptions.DuplicateKeyException;
import com.example.core.contracts.domain.exceptions.InvalidDataException;
import com.example.core.contracts.domain.exceptions.NotFoundException;
import com.example.domain.entities.Category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class CategoriesRepositoryImpl implements CategoriesRepositoryOverrides {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Category save(Category entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Category insert(Category entity) {
		if (entity.getId() != 0)
			if (em.find(Category.class, entity.getId()) != null)
				throw new DuplicateKeyException();
			else
				throw new InvalidDataException("Invalid key");
		try {
			em.persist(entity);
			em.flush();
		} catch (Exception e) {
			throw e;
		}
		return entity;
	}

	@Override
	public Category update(Category entity) {
		if (em.find(Category.class, entity.getId()) == null)
			throw new NotFoundException();
		return em.merge(entity);
	}

	@Override
	public void deleteById(Integer id) {
		em.remove(Optional.ofNullable(em.find(Category.class, id)).orElseThrow(() -> new NotFoundException()));
	}

}
