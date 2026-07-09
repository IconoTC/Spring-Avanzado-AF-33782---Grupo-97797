package com.example.core.contracts.domain.repositories;

public interface EntityWithId<K> {
	K getId();
	void setId(K id);
}
