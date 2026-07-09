package com.example.core.contracts.domain.repositories;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface InsertableAndUpdatableRepository<E extends EntityWithId<K>, K>
		extends InsertableRepository<E, K>, UpdatableRepository<E, K> {

}
