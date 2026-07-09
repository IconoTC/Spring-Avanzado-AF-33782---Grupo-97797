package com.example.core.contracts.domain.repositories;

import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.example.core.contracts.domain.exceptions.NotFoundException;

@NoRepositoryBean
public interface UpdatableRepository<E extends EntityWithId<K>, K> extends ListCrudRepository<E, K> {
	default E update(E item) throws NotFoundException {
		// Con Domain events
//		var stored = findById(item.getId())
//				.orElseThrow(NotFoundException::new);
//		BeanUtils.copyProperties(item, stored, "id");
//		return save(stored);
		// Sin Domain events
		if(!existsById(item.getId()))
			throw new NotFoundException();
		return save(item);
	}
}
