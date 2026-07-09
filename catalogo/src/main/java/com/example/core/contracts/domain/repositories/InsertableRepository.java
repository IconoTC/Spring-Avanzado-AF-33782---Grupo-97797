package com.example.core.contracts.domain.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.example.core.contracts.domain.exceptions.DuplicateKeyException;

@NoRepositoryBean
public interface InsertableRepository<E extends EntityWithId<K>, K> extends ListCrudRepository<E, K> {
	default E insert(E item) throws DuplicateKeyException {
		if(existsById(item.getId()))
			throw new DuplicateKeyException();
		return save(item);
	}
}
