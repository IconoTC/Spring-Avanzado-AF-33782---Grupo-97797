package com.example.ioc.contratos;

import java.util.List;

public interface Servicio<K, V> {
	List<V> get();
	
	V get(K id);

	void add(V item);

	void modify(V item);

	void remove(K id);
}