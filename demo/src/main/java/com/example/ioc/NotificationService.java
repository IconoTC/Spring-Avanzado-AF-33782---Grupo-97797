package com.example.ioc;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

public interface NotificationService {

	boolean hasMessages();
	
	List<String> getListado();
	
	Optional<String> getMessage(int index);
	
	void add(@NonNull String message);

	void delete(int index);

	void clear();

}