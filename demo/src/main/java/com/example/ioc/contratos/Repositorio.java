package com.example.ioc.contratos;

public interface Repositorio<T> {
	T load();
	void save(T item);
}
